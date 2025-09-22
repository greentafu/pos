const inputName = document.getElementById('name');

export function getProductPage(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/productCategoryPage',
            method:'GET',
            data: {page:page, size:size, searchText:searchText, searchCategory:searchCategory},
            success:function(data){
                totalPages=data.page.totalPages;
                if(totalPages<page) finPage=true;
                const contents = data.content;
                contents.forEach(content => {
                    const newTr = document.createElement('tr');
                    newTr.id = 'area1'+content.id;

                    [content.name, content.count].forEach(text => {
                        const newTd = document.createElement('td');
                        newTd.textContent = text;
                        newTr.appendChild(newTd);
                    });

                    defaultInsertPoint.appendChild(newTr);
                });
                if(page===2) resetSortTable();
                resolve();
            },
            error: function(xhr) { console.log('error'); }
        });
        page++;
    });
}

export function getProduct(){
    $.ajax({
        url:'/api/getProductCategory',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            divId.dataset.number = data.id;
            inputName.value = data.name;
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function saveProduct(){
    const id = Number(divId.dataset.number);
    const name = inputName.value;

    return new Promise((resolve) => {
        $.ajax({
            url:'/api/saveProductCategory',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({id:id, name:name}),
            success:function(data){
                const newTd = document.createElement('td');
                newTd.id = 'category'+data.id;

                const newDiv = document.createElement('div');
                newDiv.textContent = data.name;
                newTd.appendChild(newDiv);

                nonPlacedTr.appendChild(newTd);

                unActiveSortTableBtn();
                resolve();
            },
            error: function(xhr) {
                let response = JSON.parse(xhr.responseText);
                if (response.errors) alert(response.errors.join("\n"));
                else alert("알 수 없는 에러가 발생했습니다.");
            }
        });
    });
}

export function deleteProduct(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/deleteProductCategory',
            method:'DELETE',
            data: {id:selectedRow},
            success:function(data){
                document.getElementById('category'+selectedRow).remove();
                resolve();
            },
            error: function(xhr) {
                let response = JSON.parse(xhr.responseText);
                if (response.errors) alert(response.errors.join("\n"));
                else alert("알 수 없는 에러가 발생했습니다.");
            }
        });
    });
}

export function resetProduct(){
    inputName.value=null;
}
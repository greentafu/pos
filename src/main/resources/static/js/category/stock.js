const inputName = document.getElementById('name');

export function getStockPage(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/stockCategoryPage',
            method:'GET',
            data: {page:page, size:size, searchText:searchText},
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
                resolve();
            },
            error: function(xhr) { console.log('error'); }
        });
        page++;
    });
}

export function getStock(){
    $.ajax({
        url:'/api/getStockCategory',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            divId.dataset.number = data.id;
            inputName.value = data.name;
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function saveStock(){
    const id = Number(divId.dataset.number);
    const name = inputName.value;

    return new Promise((resolve) => {
        $.ajax({
            url:'/api/saveStockCategory',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({id:id, name:name}),
            success:function(data){
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

export function deleteStock(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/deleteStockCategory',
            method:'DELETE',
            data: {id:selectedRow},
            success:function(data){
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

export function resetStock(){
    inputName.value=null;
}
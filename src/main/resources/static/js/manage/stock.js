const inputName = document.getElementById('name');
const inputNumber = document.getElementById('number');
const selectCategory = document.getElementById('category');
const inputCost = document.getElementById('cost');
const inputUnit = document.getElementById('unit');

export function getStockPage(){
    let tempPage = (pageName==='Stock')? page:page2;
    let tempSize = (pageName==='Stock')? size:size2;
    let tempText = (pageName==='Stock')? searchText:searchText2;
    let tempSearch = (pageName==='Stock')? searchCategory:searchCategory2;

    return new Promise((resolve) => {
        $.ajax({
            url:'/api/stockPage',
            method:'GET',
            data: {page:tempPage, size:tempSize, searchText:tempText, searchCategory:tempSearch},
            success:function(data){
                if(pageName==='Stock'){
                    totalPages=data.page.totalPages;
                    if(totalPages<page) finPage=true;
                }else{
                    totalPages2=data.page.totalPages;
                    if(totalPages2<page2) finPage2=true;
                }

                const contents = data.content;
                contents.forEach(content => {
                    const newTr = document.createElement('tr');
                    if(pageName==='Stock') newTr.id = 'area1'+content.id;
                    else {
                        newTr.id = 'area2'+content.id;
                        newTr.className='area2';
                    }

                    [content.number, content.name, content.category, (content.stockCost+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원', content.unit].forEach(text => {
                        const newTd = document.createElement('td');
                        newTd.textContent = text;
                        newTr.appendChild(newTd);
                    });

                    if(pageName==='Stock') defaultInsertPoint.appendChild(newTr);
                    else addInsertPoint.appendChild(newTr);
                });

                resolve();
            },
            error: function(xhr) { console.log('error'); }
        });
        if(pageName==='Stock') page++;
        else page2++;
    });
}

export function getStock(){
    $.ajax({
        url:'/api/getStock',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            divId.dataset.number = data.id;
            inputName.value = data.name;
            inputNumber.value = data.number;
            selectCategory.value = 'category'+data.stockCategoryDTO.id;
            inputCost.value = (data.stockCost+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            inputUnit.value = data.unit;
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function saveStock(){
    const id = Number(divId.dataset.number);
    const name = inputName.value;
    const number = inputNumber.value;
    const category = Number(selectCategory.value.slice(8));
    const cost = inputCost.value;
    const unit = inputUnit.value;

    return new Promise((resolve) => {
        $.ajax({
            url:'/api/saveStock',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({id:id, number:number, name:name, stockCost:cost, unit:unit, category:category}),
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
            url:'/api/deleteStock',
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
    inputNumber.value=null;;
    selectCategory.selectedIndex = 0;
    inputCost.value=null;
    inputUnit.value=null;
}
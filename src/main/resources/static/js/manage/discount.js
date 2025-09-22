const inputName = document.getElementById('name');
const inputNumber = document.getElementById('number');
const selectCategory = document.getElementById('category');
const inputDisplayName = document.getElementById('displayName');
const inputPerCount = document.getElementById('perCount');
const inputPerProduct = document.getElementById('perProduct');
const inputAmount = document.getElementById('amount');
const inputPercent = document.getElementById('percent');
const inputPrice = document.getElementById('price');

export function getDiscountPage(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/discountPage',
            method:'GET',
            data: {page:page, size:size, searchText:searchText, searchCategory:searchCategory},
            success:function(data){
                totalPages=data.page.totalPages;
                if(totalPages<page) finPage=true;
                const contents = data.content;
                contents.forEach(content => {
                    const newTr = document.createElement('tr');
                    newTr.id = 'area1'+content.id;

                    let tempCost = content.cost;
                    let unit = '%';
                    if(content.typeValue==true) {
                        tempCost = (tempCost+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                        unit='원';
                    }
                    [content.number, content.name, content.category, tempCost+unit].forEach(text => {
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

export function getDiscount(){
    $.ajax({
        url:'/api/getDiscount',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            divId.dataset.number = data.id;
            inputName.value = data.name;
            inputNumber.value = data.number;
            selectCategory.value = 'category'+data.discountCategoryDTO.id;
            inputDisplayName.value = data.displayName;
            inputPerCount.checked = data.discountType;
            inputPerProduct.checked = !data.discountType;
            inputAmount.checked = data.typeValue;
            inputPercent.checked = !data.typeValue;
            inputPrice.value = data.discountPrice;
            changeUnit();
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function saveDiscount(){
    const id = Number(divId.dataset.number);
    const name = inputName.value;
    const number = inputNumber.value;
    const category = Number(selectCategory.value.slice(8));
    const displayName = inputDisplayName.value;
    const discountType = inputPerCount.checked;
    const typeValue = inputAmount.checked
    const price = inputPrice.value;

    return new Promise((resolve) => {
        $.ajax({
            url:'/api/saveDiscount',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({id:id, number:number, name:name, displayName:displayName, discountType:discountType,
                                typeValue:typeValue, price:price, category:category}),
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

export function deleteDiscount(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/deleteDiscount',
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

export function resetDiscount(){
    inputName.value=null;
    inputNumber.value=null;;
    selectCategory.selectedIndex = 0;
    inputDisplayName.value=null;
    inputPerCount.checked=true;
    inputPerProduct.checked=false;
    inputAmount.checked=true;
    inputPercent.checked=false;
    inputPrice.value=null;
    changeUnit();
}

export function changeUnit(){
    const won = document.getElementById('won');
    const per = document.getElementById('per');

    if(inputAmount.checked){
        won.style.display='';
        per.style.display='none';
    }else{
        won.style.display='none';
        per.style.display='';
    }
}
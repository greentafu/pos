const inputName = document.getElementById('name');
const inputNumber = document.getElementById('number');
const selectCategory = document.getElementById('category');
const inputDisplayName = document.getElementById('displayName');
const inputPrice = document.getElementById('price');
const inputCost = document.getElementById('cost');
const inputAuto = document.getElementById('auto');
const inputManual = document.getElementById('manual');

const receiptPoint = document.getElementById('receiptPoint');

export function getProductPage(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/productPage',
            method:'GET',
            data: {page:page, size:size, searchText:searchText, searchCategory:searchCategory},
            success:function(data){
                totalPages=data.page.totalPages;
                if(totalPages<page) finPage=true;
                const contents = data.content;
                contents.forEach(content => {
                    const newTr = document.createElement('tr');
                    newTr.id = 'area1'+content.id;

                    [content.number, content.name, content.category, (content.productPrice+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원'].forEach(text => {
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

export function getProduct(){
    $.ajax({
        url:'/api/getProduct',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            receiptPoint.innerHTML = '';

            const productDTO = data.productDTO;
            const receiptList = data.receiptList;

            divId.dataset.number = productDTO.id;
            inputName.value = productDTO.name;
            inputNumber.value = productDTO.number;
            selectCategory.value = productDTO.productCategoryDTO.id;
            inputDisplayName.value = productDTO.displayName;
            inputPrice.value = (productDTO.productPrice+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            inputCost.value = (productDTO.productCost+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            inputAuto.checked = productDTO.soldOutType;
            inputManual.checked = !productDTO.soldOutType;

            if(receiptList!==null){
                receiptList.forEach(rcp => {
                    const id = rcp.stockId;
                    const tempQuantity = rcp.quantity;
                    const tempCost = rcp.stockDTO.stockCost;

                    const newTr = document.createElement('tr');
                    newTr.id = 'receipt'+id;
                    newTr.dataset.price = rcp.stockDTO.stockCost;

                    const nameTd = document.createElement('td');
                    nameTd.textContent = rcp.stockDTO.name
                    newTr.appendChild(nameTd);

                    const quantityTd = document.createElement('td');
                    const innerDiv = document.createElement('div');
                    innerDiv.className = "overlap-box";
                    const innerInput = document.createElement('input');
                    innerInput.type = 'text';
                    innerInput.id = 'quantity'+id;
                    innerInput.className = 'autoComma overlap-input';
                    innerInput.style.height = '30px';
                    innerInput.oninput = function() {autoCalculation(this);};
                    innerInput.value = (tempQuantity+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                    const innerSpan = document.createElement('span');
                    innerSpan.className = 'overlap-span';
                    innerSpan.textContent = rcp.stockDTO.unit;

                    innerDiv.appendChild(innerInput);
                    innerDiv.appendChild(innerSpan);
                    quantityTd.appendChild(innerDiv);
                    newTr.appendChild(quantityTd);

                    const priceTd = document.createElement('td');
                    priceTd.id = 'price'+id;
                    priceTd.textContent = (tempQuantity*tempCost+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';
                    newTr.appendChild(priceTd);

                    const btnTd = document.createElement('td');
                    const innerBtn = document.createElement('button');
                    innerBtn.dataset.btnNum = id;
                    innerBtn.style.height = '30px';
                    innerBtn.textContent = 'X'
                    innerBtn.onclick = function() {deleteReceipt(this);};
                    btnTd.appendChild(innerBtn);
                    newTr.appendChild(btnTd);

                    receiptPoint.appendChild(newTr);
                });
            }
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function saveProduct(){
    const id = Number(divId.dataset.number);
    const name = inputName.value;
    const number = inputNumber.value;
    const category = selectCategory.value;
    const displayName = inputDisplayName.value;
    const price = inputPrice.value;
    const cost = inputCost.value;
    const soldOutType = inputAuto.checked;
    const receiptList = [];

    const allReceipt = document.querySelectorAll('tbody#receiptPoint tr');
    allReceipt.forEach(tr => {
        const tempId = Number(tr.id.slice(7));
        const tempQuantity = document.getElementById('quantity'+tempId).value;
        receiptList.push({id:tempId, quantity:tempQuantity});
    });

    return new Promise((resolve) => {
        $.ajax({
            url:'/api/saveProduct',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({id:id, number:number, name:name, displayName:displayName,
                                productPrice:price, productCost:cost, soldOutType:soldOutType,
                                productCategory:category, receiptList:receiptList}),
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

export function deleteProduct(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/deleteProduct',
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

export function resetProduct(){
    inputName.value=null;
    inputNumber.value=null;
    selectCategory.selectedIndex = 0;
    inputDisplayName.value=null;
    inputPrice.value=null;
    inputCost.value=null;
    inputAuto.checked=true
    inputManual.checked=false;
    receiptPoint.innerHTML = '';
}
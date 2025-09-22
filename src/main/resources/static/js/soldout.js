const unplacedDiv = document.querySelector('.unplaced');
const placedDiv = document.querySelector('.placed');
const toPlaceBtn = document.getElementById('toPlace');
const toNotPlaceBtn = document.getElementById('toNotPlace');

const productSelect = document.getElementById('productSelect');
const optionSelect = document.getElementById('optionSelect');
const stockSelect = document.getElementById('stockSelect');

const divSelectName = document.getElementById('selectName');
const receiptPoint = document.getElementById('receiptPoint');

const resetBtn = document.getElementById('resetSoldOut_Btn');
const saveBtn = document.getElementById('saveSoldOut_Btn');

let selected_unplaced=[];
let selected_placed=[];
let save_toTure=[];
let save_toFalse=[];
let selectType=0;
let category=0;

document.addEventListener("DOMContentLoaded", () => {
    getSoldOutPage();

    unplacedDiv.addEventListener('click', function(e){
        const tempDiv = e.target.closest('.wrapContent');
        if(tempDiv){
            getSoldOut(tempDiv);
            if(tempDiv.style.backgroundColor === ''){
                tempDiv.style.backgroundColor = '#81ACEC';
                selected_unplaced.push(tempDiv.id);
            }else{
                tempDiv.style.backgroundColor = '';
                selected_unplaced=selected_unplaced.filter(id => id !== tempDiv.id);
            }
            if(selected_unplaced.length===0) toPlaceBtn.disabled = true;
            else toPlaceBtn.disabled = false;
        }
    });
    placedDiv.addEventListener('click', function(e){
        const tempDiv = e.target.closest('.wrapContent');
        if(tempDiv){
            getSoldOut(tempDiv);
            if(tempDiv.style.backgroundColor === ''){
                tempDiv.style.backgroundColor = '#81ACEC';
                selected_placed.push(tempDiv.id);
            }else{
                tempDiv.style.backgroundColor = '';
                selected_placed=selected_placed.filter(id => id !== tempDiv.id);
            }
            if(selected_placed.length===0) toNotPlaceBtn.disabled = true;
            else toNotPlaceBtn.disabled = false;
        }
    });

    toPlaceBtn.addEventListener('click', function(){
        selected_unplaced.forEach(num => {
            const tempDiv = document.getElementById(num);
            tempDiv.parentNode.removeChild(tempDiv);

            const newDiv = document.createElement('div');
            newDiv.className = 'wrapContent';
            newDiv.id = num;
            newDiv.textContent = tempDiv.textContent;

            placedDiv.appendChild(newDiv);
            save_toTure.push(num);
            save_toFalse=save_toFalse.filter(id => id !== num);
        });
        selected_unplaced=[];
        toPlaceBtn.disabled = true;
        resetBtn.disabled=false;
        saveBtn.disabled=false;
    });
    toNotPlaceBtn.addEventListener('click', function(){
        selected_placed.forEach(num => {
            const tempDiv = document.getElementById(num);
            tempDiv.parentNode.removeChild(tempDiv);

            const newDiv = document.createElement('div');
            newDiv.className = 'wrapContent';
            newDiv.id = num;
            newDiv.textContent = tempDiv.textContent;

            unplacedDiv.appendChild(newDiv);
            save_toTure=save_toTure.filter(id => id !== num);
            save_toFalse.push(num);
        });
        selected_placed=[];
        toNotPlaceBtn.disabled = true;
        resetBtn.disabled=false;
        saveBtn.disabled=false;
    });
});

function getSoldOutPage(){
    $.ajax({
        url:'/api/soldOutPage',
        method:'GET',
        data: {selectType:selectType, category:category},
        success:function(data){
            const nonSoldOutDTO = data.nonSoldOutDTO;
            const soldOutDTO = data.soldOutDTO;

            nonSoldOutDTO.optionList?.forEach(option => {
                const newDiv = document.createElement('div');
                newDiv.className = 'wrapContent';
                newDiv.id = 'opt'+option.id;
                newDiv.textContent = option.name;
                unplacedDiv.appendChild(newDiv);
            });
            nonSoldOutDTO.productList?.forEach(product => {
                const newDiv = document.createElement('div');
                newDiv.className = 'wrapContent';
                newDiv.id = 'prd'+product.id;
                newDiv.textContent = product.name;
                unplacedDiv.appendChild(newDiv);
            });
            nonSoldOutDTO.stockList?.forEach(stock => {
                const newDiv = document.createElement('div');
                newDiv.className = 'wrapContent';
                newDiv.id = 'stc'+stock.id;
                newDiv.textContent = stock.name;
                unplacedDiv.appendChild(newDiv);
            });

            soldOutDTO.optionList?.forEach(option => {
                const newDiv = document.createElement('div');
                newDiv.className = 'wrapContent';
                newDiv.id = 'opt'+option.id;
                newDiv.textContent = option.name;
                placedDiv.appendChild(newDiv);
            });
            soldOutDTO.productList?.forEach(product => {
                const newDiv = document.createElement('div');
                newDiv.className = 'wrapContent';
                newDiv.id = 'prd'+product.id;
                newDiv.textContent = product.name;
                placedDiv.appendChild(newDiv);
            });
            soldOutDTO.stockList?.forEach(stock => {
                const newDiv = document.createElement('div');
                newDiv.className = 'wrapContent';
                newDiv.id = 'stc'+stock.id;
                newDiv.textContent = stock.name;
                placedDiv.appendChild(newDiv);
            });
        },
        error: function(xhr) { console.log('error'); }
    });
}

function getSoldOut(tempDiv){
    if(!receiptPoint) return null;

    const type = tempDiv.id.slice(0, 3);
    const id = tempDiv.id.slice(3);
    receiptPoint.innerHTML = '';

    if(type==='opt'){
        $.ajax({
            url:'/api/getOption',
            method:'GET',
            data: {id:id},
            success:function(data){
                divSelectName.textContent = data.optionDTO.name+'(상품-'+data.optionDTO.optionCategoryDTO.name+')';

                const receiptList = data.receiptList;
                receiptList?.forEach(rcp => {
                    const newTr = document.createElement('tr');

                    [rcp.stockDTO.name, (rcp.quantity+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+rcp.stockDTO.unit,
                    (rcp.stockDTO.quantity+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+rcp.stockDTO.unit].forEach(text => {
                        const newTd = document.createElement('td');
                        newTd.textContent = text;
                        newTr.appendChild(newTd);
                    });
                    receiptPoint.appendChild(newTr);
                });
            },
            error: function(xhr) { console.log('error'); }
        });
    }else if(type==='prd'){
        $.ajax({
            url:'/api/getProduct',
            method:'GET',
            data: {id:id},
            success:function(data){
                divSelectName.textContent = data.productDTO.name+'(상품-'+data.productDTO.productCategoryDTO.name+')';

                const receiptList = data.receiptList;
                receiptList?.forEach(rcp => {
                    const newTr = document.createElement('tr');

                    [rcp.stockDTO.name, (rcp.quantity+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+rcp.stockDTO.unit,
                    (rcp.stockDTO.quantity+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+rcp.stockDTO.unit].forEach(text => {
                        const newTd = document.createElement('td');
                        newTd.textContent = text;
                        newTr.appendChild(newTd);
                    });
                    receiptPoint.appendChild(newTr);
                });
            },
            error: function(xhr) { console.log('error'); }
        });
    }else if(type==='stc'){
        $.ajax({
            url:'/api/getStock',
            method:'GET',
            data: {id:id},
            success:function(data){
                divSelectName.textContent = data.name+'(재고-'+data.stockCategoryDTO.name+')';

                const newTr = document.createElement('tr');

                [data.name, '', (data.quantity+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+data.unit].forEach(text => {
                    const newTd = document.createElement('td');
                    newTd.textContent = text;
                    newTr.appendChild(newTd);
                });
                receiptPoint.appendChild(newTr);
            },
            error: function(xhr) { console.log('error'); }
        });
    }
}

function saveSoldOut(){
    $.ajax({
        url:'/api/saveSoldOut',
        method:'PUT',
        contentType: "application/json",
        data: JSON.stringify({trueList:save_toTure, falseList:save_toFalse}),
        success:function(data){ resetSoldOutPage(); },
        error: function(xhr) { console.log('error'); }
    });
}

function selectRadio(){
    const tempSelect = document.querySelector('input[name="itemType"]:checked').value;
    selectType = Number(tempSelect);

    if(selectType===0) {
        productSelect.disabled = true;
        optionSelect.disabled = true;
        stockSelect.disabled = true;
    }else if(selectType===1) {
        productSelect.disabled = false;
        optionSelect.disabled = true;
        stockSelect.disabled = true;
    }else if(selectType===2) {
        productSelect.disabled = true;
        optionSelect.disabled = false;
        stockSelect.disabled = true;
    }else if(selectType===3) {
        productSelect.disabled = true;
        optionSelect.disabled = true;
        stockSelect.disabled = false;
    }
    changeSelect();
}
function changeSelect(){
    if(selectType===0) category = 0;
    else if(selectType===1) category = Number(productSelect.value.slice(6));
    else if(selectType===2) category = Number(optionSelect.value.slice(6));
    else if(selectType===3) category = Number(stockSelect.value.slice(6));
    resetSoldOutPage();
}

function resetSoldOutPage(){
    unplacedDiv.innerHTML='';
    placedDiv.innerHTML='';
    selected_unplaced=[];
    selected_placed=[];
    save_toTure=[];
    save_toFalse=[];
    toPlaceBtn.disabled = true;
    toNotPlaceBtn.disabled = true;
    resetBtn.disabled = true;
    saveBtn.disabled = true;
    getSoldOutPage();
}
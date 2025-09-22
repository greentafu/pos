const divId = document.getElementById('idNumber');
const crudD_Btn = document.getElementById('crudD_Btn');
const crudCU_Btn = document.getElementById('crudCU_Btn');

function debounce(fn, delay) {
    let timer;
    return function (...args) {
        clearTimeout(timer);
        timer = setTimeout(() => fn.apply(this, args), delay);
    };
}

document.addEventListener("DOMContentLoaded", () => {
    pageName = document.getElementById('pageName').dataset.name;

    if(scrollArea){
        scrollStep = scrollArea.clientHeight-60;
        const listHeight = scrollArea.offsetHeight-35;
        size = Math.floor(listHeight/30)*2;

        if(searchInput){
            searchInput.addEventListener('input', debounce(() => {
                searchText = searchInput.value;
                defaultInsertPoint.innerHTML = '';
                page=1;
                finPage=false;
                getPage();
            }, 300));
        }
        if(searchSelect){
            searchSelect.addEventListener('change', function(){
                let tempCategory = searchSelect.value.slice(6);
                searchCategory = Number(tempCategory);
                defaultInsertPoint.innerHTML = '';
                page=1;
                finPage=false;
                getPage();
            });
        }
        getPage();
        showScrollArea();
        clickRow();
    }

    if(scrollArea2) {
        scrollStep2 = scrollArea.clientHeight-100;
        const listHeight = scrollArea.offsetHeight-65;
        size2 = Math.floor(listHeight/30)*2;

        if(searchInput2){
            searchInput2.addEventListener('input', debounce(() => {
                searchText2 = searchInput2.value;
                addInsertPoint.innerHTML = '';
                page2=1;
                finPage2=false;
                getPage2();
            }, 300));
        }
        if(searchSelect2){
            searchSelect2.addEventListener('change', function(){
                let tempCategory = searchSelect2.value.slice(6);
                searchCategory2 = Number(tempCategory);
                addInsertPoint.innerHTML = '';
                page2=1;
                finPage2=false;
                getPage2();
            });
        }
        getPage2();
        showScrollArea2();
        clickRow2();
    }

});

// page가져오기
function getPage(){
    const actions = { getWagePage, getPositionPage, getEmployeePage, getMailPage, getPosPage,
                     getStockPage, getDiscountPage, getOptionPage, getProductPage,
                     getStockMovementPage, getMemberPage };

    const prefix = "get";
    const postfix = "Page";
    const fnName = prefix + pageName + postfix;
    return actions[fnName]().then(() => {
        updateButtonColors();
        const tempRow = document.getElementById('area1'+selectedRow);
        if(tempRow) tempRow.style.backgroundColor = '#81ACEC';
    });
}
function getPage2(){
    const actions = { getStockPage };
    let fnName='';
    if(pageName==="Option" || pageName==='Product') fnName = "getStockPage";
    if(pageName==="Member") return null;
    return actions[fnName]().then(() => {
        updateButtonColors2();
        const tempRow2 = document.getElementById('area2'+selectedRow2);
        if(tempRow2) tempRow2.style.backgroundColor = '#81ACEC';
    });
}

// set/reset
function setCrudPage(){
    const actions = { getWage, getPosition, getEmployee, getMail, getPos,
                     getStock, getDiscount, getOption, getProduct,
                     getStockMovement, getMember };

    const prefix = "get";
    const fnName = prefix + pageName;
    actions[fnName]();
    crudD_Btn.disabled = false;
    if(pageName!=='StockMovement') crudCU_Btn.textContent = '수정';
}
function resetCrudPage(){
    const actions = { resetWage, resetPosition, resetEmployee, resetMail, resetPos,
                     resetStock, resetDiscount, resetOption, resetProduct,
                     resetStockMovement, resetMember };

    const prefix = "reset";
    const fnName = prefix + pageName;
    actions[fnName]();
    crudD_Btn.disabled = true;
    crudCU_Btn.textContent = '저장';
    divId.dataset.number="0";
}

// save/delete
function saveCrud(){
    const actions = { saveWage, savePosition, saveEmployee, saveMail, savePos,
                     saveStock, saveDiscount, saveOption, saveProduct,
                     saveStockMovement, saveMember };

    const prefix = "save";
    const fnName = prefix + pageName;

    actions[fnName]().then(() => {
        resetCrudPage();

        selectedRow='';
        defaultInsertPoint.innerHTML = '';

        page=1;
        finPage=false;
        getPage();
    });
}
function deleteCrud(){
    const actions = { deleteWage, deletePosition, deleteEmployee, deleteMail, deletePos,
                     deleteStock, deleteDiscount, deleteOption, deleteProduct,
                     deleteMember };

    const prefix = "delete";
    const fnName = prefix + pageName;
    actions[fnName]().then(() => {
        resetCrudPage();

        selectedRow='';
        defaultInsertPoint.innerHTML = '';

        page=1;
        finPage=false;
        getPage();
    });
}

// clickRow
function clickRow(){
    const scrollArea = document.querySelector('#scrollArea');
    scrollArea.addEventListener("click", (event) => {
        const rows = scrollArea.querySelectorAll('table.management tbody tr');
        const row = event.target.closest('tbody tr');
        if(row!==null) changeRowColor(rows, row, 1);
    });
}
function changeRowColor(rows, row, variable){
    const color = row.style.backgroundColor;
    if(color==='') {
        rows.forEach(temp => temp.style.backgroundColor = '');
        row.style.backgroundColor = '#81ACEC';
        if(variable===1) {
            selectedRow=row.id.slice(5);
            setCrudPage();
        }
        else if(variable===2) selectedRow2=row.id.slice(5);
        else if(variable===3) selectedRow3=row.id.slice(5);
    }else{
        row.style.backgroundColor = '';
        if(variable===1) {
            selectedRow= '';
            resetCrudPage();
        }
        else if(variable===2) selectedRow2= '';
        else if(variable===3) selectedRow3= '';
    }
}
function clickRow2(){
    const scrollArea = document.querySelector('#scrollArea2');
    scrollArea2.addEventListener("click", (event) => {
        const rows = scrollArea2.querySelectorAll('table.management tbody tr');
        const row = event.target.closest('tbody tr');
        if(row!==null) {
            if(pageName==='Option' || pageName==='Product') {
                const receiptPoint = document.getElementById('receiptPoint');
                const allTd = row.querySelectorAll('td');
                const id = row.id.slice(5);

                const existReceipt = document.getElementById('receipt'+id);
                if(!existReceipt){
                    const newTr = document.createElement('tr');
                    newTr.id = 'receipt'+id;
                    newTr.dataset.price = allTd[3].textContent.replace('원', '');

                    const nameTd = document.createElement('td');
                    nameTd.textContent = allTd[1].textContent;
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
                    const innerSpan = document.createElement('span');
                    innerSpan.className = 'overlap-span';
                    innerSpan.textContent = allTd[4].textContent;

                    innerDiv.appendChild(innerInput);
                    innerDiv.appendChild(innerSpan);
                    quantityTd.appendChild(innerDiv);
                    newTr.appendChild(quantityTd);

                    const priceTd = document.createElement('td');
                    priceTd.id = 'price'+id;
                    priceTd.textContent = '0원';
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
                }
            }else changeRowColor(rows, row, 2);
        }
    });
}

// 옵션/상품 레시피 원가 계산
function autoCalculation(input) {
    let quantity = input.value.replace(/\D/g, '');
    quantity = parseInt(quantity, 10) || 0;
    input.value = (quantity+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');

    const id = Number(input.id.slice(8));
    const perPrice = Number(document.getElementById('receipt'+id).dataset.price);
    const tempProduct = perPrice*quantity;
    document.getElementById('price'+id).textContent=(tempProduct+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';

    calculateReceipt();
}
function deleteReceipt(button){
    const id = Number(button.dataset.btnNum);
    document.getElementById('receipt'+id).remove();
    calculateReceipt();
}
function calculateReceipt(){
    const allPriceTd = document.querySelectorAll('td[id^="price"]');
    let tempAllPrice = 0;
    allPriceTd.forEach(td => {
        const tempPrice = td.textContent.slice(0, -1);
        tempAllPrice+=Number(tempPrice.replace(',', ''));
    });
    document.getElementById('cost').value=(tempAllPrice+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

// default/addPage
function openAddPage(){
    defaultPage.style.display='none';
    addPage.style.display='';

    if(pageName==='Member') getPointPage();
}
function closeAddPage(){
    addPage.style.display='none';
    defaultPage.style.display='';

    if(pageName==='Member') resetPoint();
}
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

    scrollStep = scrollArea.clientHeight-60;

    const listHeight = scrollArea.offsetHeight-35;
    size = Math.floor(listHeight/30)*2;

    getPage();
    showScrollArea();

    clickRow();

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

});

// page가져오기
function getPage(){
    const actions = { getDiscountPage, getOptionPage, getProductPage, getStockPage,
                     getDiscountBtnPage, getOptionBtnPage };

    const prefix = "get";
    const postfix = "Page";
    const fnName = prefix + pageName + postfix;
    return actions[fnName]().then(() => {
        updateButtonColors();
        const tempRow = document.getElementById('area1'+selectedRow);
        if(tempRow) tempRow.style.backgroundColor = '#81ACEC';
    });
}
// set/reset
function setCrudPage(){
    const actions = { getDiscount, getOption, getProduct, getStock,
                     getDiscountBtn, getOptionBtn };

    const prefix = "get";
    const fnName = prefix + pageName;
    actions[fnName]();
    crudD_Btn.disabled = false;
    crudCU_Btn.textContent = '수정';
}
function resetCrudPage(){
    const actions = { resetDiscount, resetOption, resetProduct, resetStock,
                     resetDiscountBtn, resetOptionBtn };

    const prefix = "reset";
    const fnName = prefix + pageName;
    actions[fnName]();
    crudD_Btn.disabled = true;
    crudCU_Btn.textContent = '저장';
    divId.dataset.number="0";
}

// save/delete
function saveCrud(){
    const actions = { saveDiscount, saveOption, saveProduct, saveStock };

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
    const actions = { deleteDiscount, deleteOption, deleteProduct, deleteStock };

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
        if(variable===1) selectedRow=row.id.slice(5);
        setCrudPage();
    }else{
        row.style.backgroundColor = '';
        if(variable===1) selectedRow= '';
        resetCrudPage();
    }
}
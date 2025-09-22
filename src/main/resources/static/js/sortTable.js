const unplacedTable = document.querySelector('.unplaced');
const placedTable = document.querySelector('.placed');
const nonPlacedTr = document.getElementById('nonPlacedTr');
const placedTr = document.getElementById('placedTr');
const toPlaceBtn = document.getElementById('toPlace');
const toNotPlaceBtn = document.getElementById('toNotPlace');

let selected_unPlaced=[];
let selected_placed=[];

let nonArrangement = [];
let arrangement = [];

document.addEventListener("DOMContentLoaded", () => {
    const sortTables = document.querySelectorAll('.sortableRow');
    sortTables.forEach(sortable => {
        new Sortable(sortable, {
            animation: 150,
            direction: 'horizontal',
            handle: '.dragTd',
            onEnd: function (evt) {
                if (evt.oldIndex !== evt.newIndex) activeSortTableBtn();
            }
        });
    });
    clickSortTable();
    clickPlaceBtn();
});

function getCategoryList(){
    let url = '/api/getDiscountCategoryList';
    if(pageName==='Option') url = '/api/getOptionCategoryList';
    else if(pageName==='Product') url = '/api/getProductCategoryList';
    else if(pageName==='OptionBtn') url = '/api/getOptionList';
    else if(pageName==='DiscountBtn') url = '/api/getDiscountList';

    $.ajax({
        url:url,
        method:'GET',
        data:{searchCategory:searchCategory},
        success:function(data){
            const nonArrayList = data.nonArrayList;
            const arrayList = data.arrayList;

            nonArrayList?.forEach(temp => {
                const newTd = document.createElement('td');
                newTd.id = 'category'+temp.id;

                const newDiv = document.createElement('div');
                newDiv.textContent = temp.name;
                newTd.appendChild(newDiv);

                nonPlacedTr.appendChild(newTd);
            });

            arrayList?.forEach(temp => {
                const newTd = document.createElement('td');
                newTd.id = 'category'+temp.id;
                newTd.classList.add("dragTd");

                const newDiv = document.createElement('div');
                newDiv.textContent = temp.name;
                newTd.appendChild(newDiv);

                const newImg = document.createElement('img');
                newImg.src = "/icon/code.png";
                newTd.appendChild(newImg);

                placedTr.appendChild(newTd);
            });
        },
        error: function(xhr) { console.log('error'); }
    });
}

// moveSortTable td
function clickSortTable(){
    unplacedTable.addEventListener('click', function(e){
        const tempTd = e.target.closest('td');
        if(tempTd.style.backgroundColor === ''){
            tempTd.style.backgroundColor = '#81ACEC';
            selected_unPlaced.push(tempTd.id);
        }else{
            tempTd.style.backgroundColor = '';
            selected_unPlaced=selected_unPlaced.filter(id => id !== tempTd.id);
        }
        if(selected_unPlaced.length===0) toPlaceBtn.disabled = true;
        else toPlaceBtn.disabled = false;
    });
    placedTable.addEventListener('click', function(e){
        const tempTd = e.target.closest('td');
        if(tempTd.style.backgroundColor === ''){
            tempTd.style.backgroundColor = '#81ACEC';
            selected_placed.push(tempTd.id);
        }else{
            tempTd.style.backgroundColor = '';
            selected_placed=selected_placed.filter(id => id !== tempTd.id);
        }
        if(selected_placed.length===0) toNotPlaceBtn.disabled = true;
        else toNotPlaceBtn.disabled = false;
    });
}
function clickPlaceBtn(){
    toPlaceBtn.addEventListener('click', function(){
        activeSortTableBtn();
        selected_unPlaced.forEach(id => {
            const tempTd = document.getElementById(id);
            tempTd.parentNode.removeChild(tempTd);

            const row = placedTable.rows[0];
            const newTd = document.createElement('td');
            newTd.className = 'dragTd';
            newTd.id = id;

            const name = document.createElement('div');
            name.textContent = tempTd.querySelector('div').textContent;
            newTd.appendChild(name);

            const image = document.createElement('img');
            image.src = '/icon/code.png';
            newTd.appendChild(image);

            row.appendChild(newTd);
        });
        selected_unPlaced=[];
        toPlaceBtn.disabled = true;
    });
    toNotPlaceBtn.addEventListener('click', function(){
        activeSortTableBtn();
        selected_placed.forEach(id => {
            const tempTd = document.getElementById(id);
            tempTd.parentNode.removeChild(tempTd);

            const row = unplacedTable.rows[0];
            const newTd = document.createElement('td');
            newTd.id = id;

            const name = document.createElement('div');
            name.textContent = tempTd.querySelector('div').textContent;
            newTd.appendChild(name);

            row.appendChild(newTd);
        });
        selected_placed=[];
        toNotPlaceBtn.disabled = true;
    });
}

// reset/save
function resetSortTable(){
    nonPlacedTr.innerHTML = '';
    placedTr.innerHTML = '';
    selected_unPlaced=[];
    selected_placed=[];
    nonArrangement = [];
    arrangement = [];
    toNotPlaceBtn.disabled = true;
    toPlaceBtn.disabled = true;

    unActiveSortTableBtn();
    getCategoryList();
}
function saveSortTable(){
    const allNonTd = nonPlacedTr.querySelectorAll('td');
    const allTd = placedTr.querySelectorAll('td');

    allNonTd.forEach(td => {
        const id = td.id.slice(8);
        nonArrangement.push(Number(id));
    });
    allTd.forEach(td => {
        const id = td.id.slice(8);
        arrangement.push(Number(id));
    });

    let url = '/api/saveDiscountCategoryArrangement';
    if(pageName==='Option') url = '/api/saveOptionCategoryArrangement';
    else if(pageName==='Product') url = '/api/saveProductCategoryArrangement';
    else if(pageName==='OptionBtn') url = '/api/saveOptionArrangement';
    else if(pageName==='DiscountBtn') url = '/api/saveDiscountArrangement';

    $.ajax({
        url:url,
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({nonArrangementList:nonArrangement, arrangementList:arrangement}),
        success:function(data){
            resetSortTable();
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}

// sortTable's btn
function unActiveSortTableBtn(){
    resetSortTable_Btn.disabled = true;
    saveSortTable_Btn.disabled = true;
}
function activeSortTableBtn(){
    resetSortTable_Btn.disabled = false;
    saveSortTable_Btn.disabled = false;
}

const radios = document.querySelectorAll('input[type=radio]');
radios.forEach(radio => {
    radio.addEventListener('change', function(){
        const id=radio.id;
        const productSelect = document.getElementById('productSelect');
        const optionSelect = document.getElementById('optionSelect');
        const stockSelect = document.getElementById('stockSelect');
        if(id==='allItem'){
            productSelect.disabled = true;
            optionSelect.disabled = true;
            stockSelect.disabled = true;
        }else if(id==='productItem'){
            productSelect.disabled = false;
            optionSelect.disabled = true;
            stockSelect.disabled = true;
        }else if(id==='optionItem'){
            productSelect.disabled = true;
            optionSelect.disabled = false;
            stockSelect.disabled = true;
        }else if(id==='stockItem'){
            productSelect.disabled = true;
            optionSelect.disabled = true;
            stockSelect.disabled = false;
        }
    });
});
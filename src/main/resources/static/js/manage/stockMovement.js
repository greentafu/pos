const divName = document.getElementById('name');
const divQuantity = document.getElementById('quantity');
const selectInoutType = document.getElementById('inoutType');
const inputPlus = document.getElementById('plus');
const inputMinus = document.getElementById('minus');
const inputInoutQuantity = document.getElementById('inoutQuantity');
const inputNotes = document.getElementById('notes');
const inputSoldOut = document.getElementById('soldOut');

export function getStockMovementPage(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/stockPage',
            method:'GET',
            data: {page:page, size:size, searchText:searchText, searchCategory:searchCategory},
            success:function(data){
                totalPages=data.page.totalPages;
                if(totalPages<page) finPage=true;

                const contents = data.content;
                contents.forEach(content => {
                    const newTr = document.createElement('tr');
                    newTr.id = 'area1'+content.id;

                    [content.number, content.name, content.category, (content.stockCost+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원', content.unit].forEach(text => {
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

export function getStockMovement(){
    $.ajax({
        url:'/api/getStock',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            divId.dataset.number = data.id;
            divName.textContent = data.name+'('+(data.number+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+', '+data.stockCategoryDTO.name+')';
            divQuantity.textContent = data.quantity+data.unit;
            selectInoutType.selectedIndex = 0;
            inputPlus.checked = true;
            inputMinus.checked = false;
            inputInoutQuantity.value = 0;
            inputNotes.value = null;
            inputSoldOut.checked = data.soldOut;
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function saveStockMovement(){
    const id = Number(divId.dataset.number);
    const inoutType = selectInoutType.value;
    const plusType = inputPlus.checked;
    const quantity = inputInoutQuantity.value;
    const notes = inputNotes.value;

    return new Promise((resolve) => {
        $.ajax({
            url:'/api/saveStockMovement',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({id:id, movementAmount:quantity, plusType:plusType, typeValue:inoutType, notes:notes}),
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

export function resetStockMovement(){
    divName.textContent = '';
    divQuantity.textContent = '';
    selectInoutType.selectedIndex = 0;
    inputPlus.checked = true;
    inputMinus.checked = false;
    inputInoutQuantity.value = 0;
    inputNotes.value = null;
    inputSoldOut.checked = false;
}

export function changeInoutType(){
    inputInoutQuantity.value = 0;
    const type = Number(selectInoutType.value);
    if(type>=0 && type <4){
        inputPlus.disabled=true;
        inputMinus.disabled=true;
        inputPlus.checked=true;
    }else if(type>=4 && type <8){
        inputPlus.disabled=true;
        inputMinus.disabled=true;
        inputMinus.checked=true;
    }else{
        inputPlus.disabled=false;
        inputMinus.disabled=false;
    }
}

export function changeSoldOut(){
    const id = Number(divId.dataset.number);
    const soldOutType = inputSoldOut.checked;
    $.ajax({
        url:'/api/updateStockSoldOut',
        method:'PUT',
        contentType: "application/json",
        data: JSON.stringify({id:id, soldOutType:soldOutType})
    });
}
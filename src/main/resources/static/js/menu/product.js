const divId = document.getElementById('idNumber');
const crudD_Btn = document.getElementById('crudD_Btn');
const crudCU_Btn = document.getElementById('crudCU_Btn');

let indexList=[];
let spaceList=[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28];

const spanName = document.getElementById('name');
const spanNumber = document.getElementById('number');
const spanPrice = document.getElementById('price');
const colorBox = document.getElementById('colorBox');
const sizeBox = document.getElementById('sizeBox');
const option1 = document.getElementById('option1');
const option2 = document.getElementById('option2');
const preBtn = document.getElementById('preBtn');
const postBtn = document.getElementById('postBtn');

const saveBtn = document.getElementById('saveBtn');
const resetBtn = document.getElementById('resetBtn');

function debounce(fn, delay) {
    let timer;
    return function (...args) {
        clearTimeout(timer);
        timer = setTimeout(() => fn.apply(this, args), delay);
    };
}

document.addEventListener("DOMContentLoaded", () => {
    searchSelect.addEventListener('change', function(){
        const temp = Number(searchSelect.value.slice(6));
        clickCategory(temp);
    });

    scrollStep = scrollArea.clientHeight-60;
    const listHeight = scrollArea.offsetHeight-35;
    size = Math.floor(listHeight/30)*2;
});

// page가져오기
function getPage(){
    $.ajax({
        url:'/api/productPage',
        method:'GET',
        data: {page:page, size:size, searchCategory:searchCategory},
        success:function(data){
            totalPages=data.page.totalPages;
            if(totalPages<page) finPage=true;
            const contents = data.content;
            contents?.forEach(content => {
                const newTr = document.createElement('tr');
                newTr.id = 'area1'+content.id;

                [content.number, content.name, content.category, (content.productPrice+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원'].forEach(text => {
                    const newTd = document.createElement('td');
                    newTd.textContent = text;
                    newTr.appendChild(newTd);
                });

                defaultInsertPoint.appendChild(newTr);
            });

            updateButtonColors();
            const tempRow = document.getElementById('area1'+selectedRow);
            if(tempRow) tempRow.style.backgroundColor = '#81ACEC';
        },
        error: function(xhr) { console.log('error'); }
    });
    page++;
}

// set/reset
function setCrudPage(){
    $.ajax({
        url:'/api/getProductBtn',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            const productDTO = data.productDTO;
            const productBtnDTO = data.productBtnDTO;

            if(productDTO!==null){
                divId.dataset.number = productDTO.id;
                spanName.textContent = productDTO.displayName;
                spanNumber.textContent = productDTO.number;
                spanPrice.textContent = productDTO.productPrice;

                const allBtn = document.querySelectorAll('.productBtn');
                allBtn.forEach(btn => {
                    if (Number(btn.id.slice(3)) === Number(productDTO.id)) btn.style.border = '5px solid #FCA400';
                    else btn.style.border = '';
                });

                if(menuMap.has(productDTO.id+'')) {
                    const menuBtn = menuMap.get(productDTO.id+'');
                    colorSizeDisplay(1);

                    const colorBoxes = document.querySelectorAll('.crudColor');
                    colorBoxes.forEach(checkbox => {
                        if (Number(checkbox.id.slice(5)) === Number(menuBtn.color)) checkbox.checked = true;
                        else checkbox.checked = false;
                    });

                    const sizeBoxes = document.querySelectorAll('.crudSize');
                    sizeBoxes.forEach(checkbox => {
                        if (Number(checkbox.id.slice(6)) === Number(menuBtn.size)) checkbox.checked = true;
                        else checkbox.checked = false;
                    });

                }else colorSizeDisplay(0);
            }

            if(productBtnDTO!==null){
                changeColorBox(productBtnDTO.color);
                changeSizeBox(productBtnDTO.size);
            }
        },
        error: function(xhr) { console.log('error'); }
    });
}
function resetCrudPage(){
    divId.dataset.number="0";
    spanName.textContent = '';
    spanNumber.textContent = '';
    spanPrice.textContent = '';

    changeColorBox(0);
    colorBox.style.display='none'

    changeSizeBox(1);
    sizeBox.style.display='none';

    crudCU_Btn.disabled = true;
    crudD_Btn.disabled = true;
}

// insert/delete
function insertBtn(){
    saveBtn.disabled = false;
    resetBtn.disabled = false;
    const id = divId.dataset.number;

    if(spaceList.length === 0) alert('빈자리가 존재하지 않습니다.');
    else{
        const min = Math.min(...spaceList);
        const insertBox = document.querySelector(`[data-index='${min}']`);

        const newDiv = document.createElement('div');
        newDiv.className = 'productBtn bord-1b';
        newDiv.id = 'btn'+id;
        newDiv.style.cursor = "grab";
        newDiv.setAttribute('draggable', 'true');
        newDiv.ondragstart = drag;
        newDiv.onclick = () => clickMenu(id);
        newDiv.style.backgroundColor = 'white';
        newDiv.dataset.size = 1;
        newDiv.style.border = '5px solid #FCA400';

        const newName = document.createElement('span');
        newName.className = 'btnName';
        newName.textContent = spanName.textContent;
        newDiv.appendChild(newName);

        const newPrice = document.createElement('span');
        newPrice.className = 'btnPrice fcor-r';
        newPrice.textContent = '('+spanPrice.textContent.replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원)';
        newDiv.appendChild(newPrice);

        insertBox.appendChild(newDiv);

        changeColorBox(0);
        changeSizeBox(1);
        colorSizeDisplay(1);
        indexSpaceList(min, null, id);
    }
}
function indexSpaceList(temp1, temp2, id){
    if(temp1!==null){
        indexList.push(Number(temp1));
        spaceList=spaceList.filter(num => num != Number(temp1));
        menuMap.set(id, { page:menuPage , index:temp1 , color:0, size:1, name:spanName.textContent, price:spanPrice.textContent });
    }
    if(temp2!==null){
        indexList=indexList.filter(num => num != Number(temp2));
        spaceList.push(Number(temp2));
        menuMap.delete(id);
    }
}
function deleteBtn(){
    saveBtn.disabled = false;
    resetBtn.disabled = false;
    const id = divId.dataset.number;
    const temp2 = menuMap.get(id).index;
    document.getElementById('btn'+id).remove();
    indexSpaceList(null, temp2, id);
    colorSizeDisplay(0);
}

// save/reset
function saveMenuBtn(){
    const menuMapObj = Object.fromEntries(menuMap);

    $.ajax({
        url:'/api/saveMenuBtn',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({searchCategory:searchCategory, menuMap:menuMapObj}),
        success:function(data){
            getMenuList(getMenuPage);
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}
function resetMenuBtn(){
    getMenuList(getMenuPage);
    document.getElementById('area1'+selectedRow).click();
}

// clickRow
function clickRow(){
    const scrollArea = document.querySelector('#scrollArea');
    scrollArea.addEventListener("click", (event) => {
        const rows = scrollArea.querySelectorAll('table.management tbody tr');
        const row = event.target.closest('tbody tr');
        if(row!==null) {
            const color = row.style.backgroundColor;
            if(color==='') {
                rows.forEach(temp => temp.style.backgroundColor = '');
                row.style.backgroundColor = '#81ACEC';
                selectedRow=row.id.slice(5);
                setCrudPage();
            }else{
                document.getElementById('btn'+selectedRow).style.border = '';
                row.style.backgroundColor = '';
                selectedRow= '';
                resetCrudPage();
            }
        }
    });
}
// click menuBtn
function clickMenu(id){
    document.getElementById('area1'+id).click();
}

// color checkbox
function colorSizeDisplay(type){
    if(type===1) {
        colorBox.style.display='';
        sizeBox.style.display='';
        crudCU_Btn.disabled = true;
        crudD_Btn.disabled = false;
    }else{
        colorBox.style.display='none';
        sizeBox.style.display='none';
        crudCU_Btn.disabled = false;
        crudD_Btn.disabled = true;
    }
}
function changeColorBox(color){
    const id = divId.dataset.number;
    if(menuMap.has(id)){
        if(menuMap.get(id).color!==color) {
            saveBtn.disabled = false;
            resetBtn.disabled = false;
        }
        menuMap.get(id).color = color;
        const tempBtn = document.getElementById('btn'+id);
        if(tempBtn) tempBtn.style.backgroundColor = colorList[color];

    }

    const colorBoxes = document.querySelectorAll('.crudColor');
    colorBoxes.forEach(checkbox => {
        if (Number(checkbox.id.slice(5)) === Number(color)) checkbox.checked = true;
        else checkbox.checked = false;
    });
}
function changeSizeBox(size){
    const id = divId.dataset.number;
    if(menuMap.has(id)){
        const existMap = menuMap.get(id);
        if(existMap.size===1 && size===2){
            const existIndex = existMap.index;
            const min = Math.floor(existIndex/5);

            let checkIndexList = [min*5, min*5+1, min*5+2, min*5+3];
            if(min!==5) checkIndexList.push(min*5+4);
            checkIndexList = checkIndexList.filter(num => num != existIndex);

            const checkIndexSet = new Set(checkIndexList);
            const existSpaceList = spaceList.filter(item => checkIndexSet.has(item));

            if(existSpaceList.length===0) {
                option2.checked = false;
                return alert('빈자리가 존재하지 않습니다.');
            }
            else{
                const closestIndex = existSpaceList.reduce((prev, curr) =>
                    Math.abs(curr - existIndex) < Math.abs(prev - existIndex) ? curr : prev
                );
                if(closestIndex > existIndex){
                    if(Math.abs(closestIndex-existIndex)===1){
                        indexList.push(closestIndex);
                        spaceList = spaceList.filter(num => num != closestIndex);
                    }else{
                        for(let i=existIndex+1; i<closestIndex; i++){
                            const existCell = document.querySelector(`[data-index='${i}']`);
                            const existBtn = existCell.querySelector('.productBtn');
                            if(existBtn) menuMap.get(existBtn.id.slice(3)).index = i+1;
                        }

                        indexList.push(closestIndex);
                        spaceList = spaceList.filter(num => num != closestIndex);
                    }
                }else{
                    if(Math.abs(closestIndex-existIndex)===1){
                        indexList.push(closestIndex);
                        spaceList = spaceList.filter(num => num != closestIndex);

                        existMap.index = closestIndex;
                    }else{
                        for(let i=existIndex-1; i>closestIndex; i--){
                            const existCell = document.querySelector(`[data-index='${i}']`);
                            const existBtn = existCell.querySelector('.productBtn');
                            if(existBtn) menuMap.get(existBtn.id.slice(3)).index = i-1;
                        }

                        indexList.push(closestIndex);
                        spaceList = spaceList.filter(num => num != closestIndex);

                        existMap.index = existIndex-1;
                    }
                }
                existMap.size = 2;
                saveBtn.disabled = false;
                resetBtn.disabled = false;
            }
        }else if(existMap.size===2 && size===1){
            const existIndex = menuMap.get(id).index;
            indexList = indexList.filter(num => num != existIndex+1);
            spaceList.push(existIndex+1);

            existMap.size = 1;
            saveBtn.disabled = false;
            resetBtn.disabled = false;
        }
        getMenuPage();
    }

    const sizeBoxes = document.querySelectorAll('.crudSize');
    sizeBoxes.forEach(checkbox => {
        if (Number(checkbox.id.slice(6)) === Number(size)) checkbox.checked = true;
        else checkbox.checked = false;
    });
}

// drag&drop
function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}
function allowDrop(ev) {
    ev.preventDefault();
}
function drop(ev) {
    saveBtn.disabled = false;
    resetBtn.disabled = false;
    ev.preventDefault();
    const btnId = ev.dataTransfer.getData("text");
    const btn = document.getElementById(btnId);
    const dropCell = ev.target.closest('.drag-cell');

    if (dropCell && !dropCell.querySelector('.productBtn')) {
        const dragCell = btn.closest('.drag-cell');
        const existIndex = Number(dragCell.dataset.index);
        const newIndex = Number(dropCell.dataset.index);

        const existMap = menuMap.get(btnId.slice(3));
        existMap.index = newIndex;

        indexList.push(newIndex);
        indexList = indexList.filter(num => num != existIndex);
        spaceList.push(existIndex);
        spaceList = spaceList.filter(num => num != newIndex);
        if(existMap.size===2){
            indexList = indexList.filter(num => num != existIndex+1);
            spaceList.push(existIndex+1);
            existMap.size=1;
        }

        getMenuPage();

        if(Number(divId.dataset.number)===Number(btnId.slice(3))) changeSizeBox(1);
    }
}
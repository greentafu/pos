let selectedNum='';
let selectedName='';
let color='white';
let size='1';
let flag=0;

let indexList=[];
let spaceList=[];

document.addEventListener("DOMContentLoaded", () => {
    const rows = document.querySelectorAll('#crudList tbody tr');
    const btns = document.querySelectorAll('.drag');
    const colors = document.querySelectorAll('.crudColor');
    const sizes = document.querySelectorAll('.crudSize');

    // indexList 채우기
    btns.forEach(btn => {
        const dragCell = btn.closest('.drag-cell');
        const index = Number(dragCell.dataset.index);
        indexList.push(index);
    });
    // spaceList 채우기
    for(let i=28; i>=0; i--) {
        if(!indexList.includes(i)) spaceList.push(i);
    }

    // 테이블 클릭시
    rows.forEach(row => {
        row.addEventListener('click', ()=> {
            btns.forEach(b => b.style.border = '');
            flag=1;
            
            if(selectedRow!==''){
                selectedNum = row.querySelector('td.selectedNum').textContent;
                selectedName = row.querySelector('td.selectedName').textContent;
                const selectedBtn=document.getElementById('btn'+selectedRow);
                if(selectedBtn){
                    selectedBtn.style.border='5px solid #FCA400';
                    color=selectedBtn.style.backgroundColor;
                    size=selectedBtn.dataset.size;
                    flag=2;
                }
            }
            changeCrudBox(selectedNum, selectedName, color, size, flag);
        });
    });

    // 버튼 클릭시
    btns.forEach(selectedBtn => {
        selectedBtn.addEventListener('click', ()=> {
            rows.forEach(r => r.style.backgroundColor = '');
            btns.forEach(b => b.style.border = '');

            selectedRow=selectedBtn.id.slice(3);
            if(selectedRow!==''){
                const selectedTr = document.getElementById(selectedRow);
                selectedTr.style.backgroundColor="#81ACEC";

                selectedNum = selectedTr.querySelector('td.selectedNum').textContent;
                selectedName = selectedTr.querySelector('td.selectedName').textContent;

                selectedBtn.style.border='5px solid #FCA400';
                color=selectedBtn.style.backgroundColor;
                size=selectedBtn.dataset.size;
                flag=2;
            }
            changeCrudBox(selectedNum, selectedName, color, size, flag);
        });
    });
    
    // 색상 버튼 클릭시
    colors.forEach(checkbox => {
        checkbox.addEventListener('click', ()=> {
            color=checkbox.style.backgroundColor;
            changeColorBox(color);
            if(selectedRow!=='') {
                const changeBtn=document.getElementById('btn'+selectedRow);
                if(changeBtn) changeBtn.style.backgroundColor=color;
            }
        });
    });

    // 사이즈 버튼 클릭시
    sizes.forEach(checkbox => {
        checkbox.addEventListener('click', ()=> {
            size=checkbox.id.slice(6);
            changeSizeBox(size);
            if(selectedRow!==''){
                const changeBtn=document.getElementById('btn'+selectedRow);
                const dragCell=changeBtn.closest('.drag-cell');
                const index = dragCell.dataset.index;
                const originSize = changeBtn.dataset.size;
                changeBtnSize(index, originSize, size);
            }
        });
    });
});

// crud 표현 함수(아래 셋 포함)
function changeCrudBox(num, name, col, si, fla){
    const crudName = document.getElementById('crudName');
    const crudNumber = document.getElementById('crudNumber');

    crudNumber.textContent = num;
    crudName.textContent = name;
    changeColorBox(col);
    changeSizeBox(si);
    crudBtn(fla);

    selectedNum='';
    selectedName='';
    color='white';
    size='1';
    flag=0;
}
// 색상checkbox checked 함수
function changeColorBox(color){
    const colorBoxes = document.querySelectorAll('.crudColor');
    colorBoxes.forEach(checkbox => {
        if (checkbox.style.backgroundColor === color) checkbox.checked = true;
        else checkbox.checked = false;
    });
}
// 사이즈checkbox checked 함수
function changeSizeBox(size){
    const sizeBoxes = document.querySelectorAll('.crudSize');
    sizeBoxes.forEach(checkbox => {
        if (checkbox.id.slice(6) === size) checkbox.checked = true;
        else checkbox.checked = false;
    });
}
// 추가/변경/삭제 버튼 함수
function crudBtn(flag){
    const createBtn = document.getElementById('createBtn');
    const deleteBtn = document.getElementById('deleteBtn');
    
    if(flag===0) {
        createBtn.disabled = true;
        deleteBtn.disabled = true;
        createBtn.innerText = '추가';
    }else if(flag===1) {
        createBtn.disabled = false;
        deleteBtn.disabled = true;
        createBtn.innerText = '추가';
    }else{
        createBtn.disabled = false;
        deleteBtn.disabled = false;
        createBtn.innerText = '변경';
    }
}
// 버튼 크기 변경 함수
function changeBtnSize(ind, os, cs){
    const index=Number(ind);
    const originSize=Number(os);
    const size=Number(cs);

    const preCell = document.querySelector(`[data-index='${index}']`);
    const postCell = document.querySelector(`[data-index='${index + 1}']`);
    const selectedBtn = preCell.querySelector('.drag');

    const line=Math.floor(index/5);
    const preFiltered=[];
    const postFiltered=[];

    if(originSize!==size){
        if(size===1){
            selectedBtn.dataset.size='1';
            preCell.style.gridColumn = 'span 1';
            if(postCell) removeAtt(postCell);

            indexList=indexList.filter(num => num !== index+1);
            spaceList.push(Number(index+1));
        }else {
            for(let i=0; i<5; i++){
                const temp=i+line*5;
                if(spaceList.includes(temp)) {
                    if(temp>index) postFiltered.push(temp);
                    else preFiltered.push(temp);
                }
            }

            if(preFiltered.length+postFiltered.length===0){
                alert('자리가 존재하지 않습니다.');
                document.getElementById('option1').checked=true;
                document.getElementById('option2').checked=false;
            }else{
                pushOneSpace(index, preFiltered, postFiltered);
            }
        }
    }
}
// 버튼 한 칸씩 밀림 함수
function pushOneSpace(ind, preFiltered, postFiltered){
    const index=Number(ind);
    let preCell = document.querySelector(`[data-index='${index}']`);
    let postCell = document.querySelector(`[data-index='${index + 1}']`);
    let closest;

    if (postFiltered.length > 0) {
        // 뒤로 땡기기
        closest = postFiltered.reduce((prev, curr) => {
            return Math.abs(curr - index) < Math.abs(prev - index) ? curr : prev;
        });
        for(let i=closest; i>index; i--){
            let preBtn = document.querySelector(`[data-index='${i-1}']`);
            let postBtn = document.querySelector(`[data-index='${i}']`);
            if(i!=index+1) getAtt(postBtn, preBtn);
        }
    } else {
        // 앞으로 땡기기
        closest = preFiltered.reduce((prev, curr) => {
            return Math.abs(curr - index) < Math.abs(prev - index) ? curr : prev;
        });
        for(let i=closest; i<index; i++){
            let preBtn = document.querySelector(`[data-index='${i}']`);
            let postBtn = document.querySelector(`[data-index='${i+1}']`);
            getAtt(preBtn, postBtn);     
        }
        preCell=document.querySelector(`[data-index='${index - 1}']`);
        postCell=document.querySelector(`[data-index='${index}']`);
    }
    indexList.push(Number(closest));
    spaceList=spaceList.filter(num => num != Number(closest));

    const selectedBtn=preCell.querySelector('.drag');
    selectedBtn.dataset.size='2';
    preCell.style.gridColumn = 'span 2';
    postCell.style.display = 'none';
}
// 버튼 속성 부여
function getAtt(getBtn, removeBtn){
    const temp = removeBtn.querySelector('.drag');
    if(temp) getBtn.appendChild(temp);
    getBtn.style.display = removeBtn.style.display;
    getBtn.style.gridColumn = removeBtn.style.gridColumn;
}
// 버튼 속성 제거
function removeAtt(removeBtn){
    removeBtn.removeChild;
    removeBtn.style.display='';
    removeBtn.style.gridColumn='';
}

// drag&drop
function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}
function allowDrop(ev) {
    ev.preventDefault();
}
function drop(ev) {
    ev.preventDefault();
    const btnId = ev.dataTransfer.getData("text");
    const btn = document.getElementById(btnId);
    const cell = ev.target.closest('.drag-cell');

    if (cell && !cell.querySelector('.drag')) {
        const removeCell=btn.closest('.drag-cell');
        const removeIndex=Number(removeCell.dataset.index);
        const removeBtn=removeCell.querySelector('.drag');
        const removeSize=Number(removeBtn.dataset.size);
        const removeCellNext = document.querySelector(`[data-index='${removeIndex+1}']`);

        const getCell=ev.target;
        const getIndex=getCell.dataset.index;

        indexList.push(Number(getIndex));
        indexList=indexList.filter(num => num != Number(removeIndex))
        spaceList.push(Number(removeIndex));
        spaceList=spaceList.filter(num => num != Number(getIndex));

        if(removeSize===2){
            removeCell.style.gridColumn = 'span 1';
            removeCellNext.style.display='';
            removeBtn.dataset.size=1;
            document.getElementById('option1').checked=true;
            document.getElementById('option2').checked=false;

            indexList=indexList.filter(num => num != Number(removeIndex+1));
            spaceList.push(Number(removeIndex+1));
        }

        ev.target.appendChild(btn);
    }
}
let selectedRowDetail='';

const categoryBox = document.getElementById('categoryBox');
const firstCategory = document.getElementById('firstCategory');

let categoryPage=1;
let categoryTotalPages=0;

let menuPage=1;
let menuMap = new Map();

let colorList = ['white', '#BCCBF7', '#FFC4C4', '#D2FDD2', '#FCFFD6', '#FBCDFF'];

document.addEventListener("DOMContentLoaded", () => {
    pageName = document.getElementById('pageName').dataset.name;
    const temp = firstCategory.textContent;

    if(temp!==null){
        searchCategory = Number(temp);
        getCategoryPage();
        getMenuList(getMenuPage);
        getPage();
        showScrollArea();
    }
    clickRow();
});

function getPreCategory(){
    categoryPage--;
    getCategoryPage();
}
function getPostCategory(){
    categoryPage++;
    getCategoryPage();
}
// menu categoryBox
function getCategoryPage(){
    const allCategory = document.querySelectorAll('.categoryBtn');
    allCategory.forEach(temp => temp.remove());

    $.ajax({
        url:'/api/menuCategory',
        method:'GET',
        data: {page:categoryPage},
        success:function(data){
            categoryTotalPages=data.page.totalPages;

            const content = data.content;
            for(let i=0; i<9; i++){
                const newBtn = document.createElement('button');
                newBtn.className = 'menu categoryBtn';

                const temp = content[i];
                if(temp==null) newBtn.disabled=true;
                else {
                    newBtn.id = 'category'+temp.id;
                    newBtn.textContent = temp.name;
                    newBtn.onclick = () => clickCategory(temp.id);
                    if(temp.id===searchCategory) newBtn.classList.add('selected');
                }

                categoryBox.appendChild(newBtn);
            }

            const categoryPreBtn = document.getElementById('categoryPreBtn');
            const categoryPostBtn = document.getElementById('categoryPostBtn');

            if(categoryPage===1) categoryPreBtn.disabled = true;
            else categoryPreBtn.disabled = false;

            if(categoryPage<categoryTotalPages) categoryPostBtn.disabled = false;
            else categoryPostBtn.disabled = true;
        },
        error: function(xhr) { console.log('error'); }
    });
}

// menu productBox
function getPreMenu(){
    menuPage--;
    getMenuPage();
}
function getPostMenu(){
    menuPage++;
    getMenuPage();
}
function getMenuList(callback){
    menuMap = new Map();
    $.ajax({
        url:'/api/getMenu',
        method:'GET',
        data: {searchCategory:searchCategory},
        success:function(data){
            if(pageName==='Product'){
                if(saveBtn) saveBtn.disabled = true;
                if(resetBtn) resetBtn.disabled = true;
            }

            if(Array.isArray(data) && data.length > 0){
                data.forEach(temp => {
                    const productDTO = temp.productDTO;

                    menuMap.set(temp.id+'', { page:temp.page , index:temp.indexValue,
                     color:temp.color, size:temp.size, name:productDTO.displayName, price:productDTO.productPrice });
                });
            }
            callback();

        },
        error: function(xhr) { console.log('error'); }
    });
}
function getMenuPage(){
    if(pageName==='Product'){
        indexList=[];
        spaceList=[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28];
    }

    const allBtn = document.querySelectorAll('.productBtn');
    allBtn.forEach(btn => btn.remove());

    const allCell = document.querySelectorAll('.drag-cell');
    allCell.forEach(cell => {
        cell.style.display='';
        cell.style.gridColumn='';
    });

    menuMap.forEach((value, id) => {
        if(value.page===menuPage){
            const insertBox = document.querySelector(`[data-index='${value.index}']`);

            const newDiv = document.createElement('div');
            newDiv.className = 'productBtn bord-1b';
            newDiv.id = 'btn'+id;
            newDiv.onclick = () => clickMenu(id);
            if(pageName!=='Product') newDiv.style.cursor = "pointer";
            if(pageName==='Product'){
                newDiv.style.cursor = "grab";
                newDiv.classList.add('drag');
                newDiv.setAttribute('draggable', 'true');
                newDiv.ondragstart = drag;
                if(selectedRow===id) newDiv.style.border = '5px solid #FCA400';
            }
            newDiv.style.backgroundColor = colorList[value.color];
            newDiv.dataset.size = value.size;

            const newName = document.createElement('span');
            newName.className = 'btnName';
            newName.textContent = value.name
            newName.style.pointerEvents = "none";
            newDiv.appendChild(newName);

            const newPrice = document.createElement('span');
            newPrice.className = 'btnPrice fcor-r';
            newPrice.textContent = '('+(value.price+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원)';
            newPrice.style.pointerEvents = "none";
            newDiv.appendChild(newPrice);

            insertBox.appendChild(newDiv);

            if(value.size===2) {
                insertBox.style.gridColumn = 'span 2';
                document.querySelector(`[data-index='${value.index+1}']`).style.display = 'none';
            }
            if(pageName==='Product'){
                indexList.push(value.index);
                spaceList = spaceList.filter(num => num != value.index);
                if(value.size===2){
                    indexList.push(value.index+1);
                    spaceList = spaceList.filter(num => num != value.index+1);
                }
            }
        }
    });

    const menuPreBtn = document.getElementById('menuPreBtn');
    const menuPostBtn = document.getElementById('menuPostBtn');

    if(menuPage===1) menuPreBtn.disabled = true;
    else menuPreBtn.disabled = false;

    if(menuPage===3) menuPostBtn.disabled = true;
    else menuPostBtn.disabled = false;
}

// click category
function clickCategory(temp){
    menuMap = new Map();
    const allCategory = document.querySelectorAll('.categoryBtn');
    allCategory.forEach(btn => {
        if (Number(btn.id.slice(8)) === Number(temp)) btn.classList.add('selected');
        else btn.classList.remove('selected');
    });
    const allBtn = document.querySelectorAll('.productBtn');
    allBtn.forEach(btn => btn.remove());

    menuPage=1;
    searchCategory = Number(temp);
    selectedRow = '';
    getMenuList(getMenuPage);

    if(pageName==='Product'){
        indexList=[];
        spaceList=[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28];
        defaultInsertPoint.innerHTML = '';
        page=1;
        finPage=false;
        getPage();
        resetCrudPage();
        if(searchSelect) searchSelect.value = 'search'+temp;
    }
}
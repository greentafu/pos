document.addEventListener("DOMContentLoaded", () => {
    getPage(1);
    getPage(2);
});
function getPage(page){
    $.ajax({
        url:'/api/screenArrangement',
        method:'GET',
        data: { page: page },
        success:function(data){
            data.forEach(screen => {
                const id=screen.screenId;
                const indexValue=screen.indexValue;
                const name=screen.screenDTO.name;
                const icon=screen.screenDTO.imgUrl;

                const boxDiv = document.querySelector(`.drag-cell[data-page="${page}"][data-index="${indexValue}"]`);

                const newBtn = document.createElement('button');
                newBtn.id = 'btn-'+id;
                newBtn.className = 'productBtn bord-1b';
                newBtn.style.cursor = "grab";
                newBtn.style.backgroundColor = 'white';
                newBtn.setAttribute('draggable', 'true');
                newBtn.ondragstart = drag;

                const newImg = document.createElement('img');
                newImg.setAttribute('draggable', 'false');
                newImg.src = icon;
                newBtn.appendChild(newImg);

                const newH2 = document.createElement('h2');
                newH2.textContent = name;
                newBtn.appendChild(newH2);

                boxDiv.appendChild(newBtn);
            });
        }
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
    ev.preventDefault();
    const btnId = ev.dataTransfer.getData("text");
    const btn = document.getElementById(btnId);
    const cell = ev.target.closest('.drag-cell');

    if (cell && !cell.querySelector('.productBtn')) {
        cell.appendChild(btn);
        saveHomeBtn(btnId.slice(4), cell.dataset.page, cell.dataset.index);
    }
}

function saveHomeBtn(id, page, index){
    $.ajax({
        url:'/api/saveHomeBtn',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({id:Number(id), page:Number(page), index:Number(index)})
    });
}
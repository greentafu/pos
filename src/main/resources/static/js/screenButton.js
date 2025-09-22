document.addEventListener("DOMContentLoaded", () => {
    const name=document.getElementById('pageName').dataset.name;
    const page=(name==='page1')?1:2;
    $.ajax({
        url:'/api/screenArrangement',
        method:'GET',
        data: { page: page },
        success:function(data){
            data.forEach(screen => {
                const page=screen.page;
                const indexValue=screen.indexValue;
                const name=screen.screenDTO.name;
                const icon=screen.screenDTO.imgUrl;
                const url=screen.screenDTO.url;

                const boxDiv = document.querySelector(`.box[data-index="${indexValue}"]`);
                boxDiv.classList.remove('cor-lg');

                const newBtn = document.createElement('button');
                newBtn.className = 'home';
                newBtn.onclick = function() {window.location.href = url;}
                if(name==='주문관리'){
                    if(document.getElementById('storeClose').disabled) newBtn.disabled = true;
                }

                const newImg = document.createElement('img');
                newImg.src = icon;
                newBtn.appendChild(newImg);

                const newH2 = document.createElement('h2');
                newH2.textContent = name;
                newBtn.appendChild(newH2);

                boxDiv.appendChild(newBtn);
            });
        },
        error: function(xhr) {
            window.location.href = "/store/affiliation";
        }
    });
});
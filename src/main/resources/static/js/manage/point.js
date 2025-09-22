const inputDeletePoint = document.getElementById('deletePoint');
const inputAddPoint = document.getElementById('addPoint');

export function getPointPage(){
    const id = Number(divId.dataset.number);
    if(id===0) {
        alert('조회할 회원을 선택해주세요.');
        closeAddPage();
        return null;
    }
    return new Promise((resolve) => {
        page2 = 1;
        finPage2=false;
        $.ajax({
            url:'/api/pointPage',
            method:'GET',
            data: {page:page2, size:size2, id:id},
            success:function(data){
                totalPages2=data.page.totalPages;
                if(totalPages2<page2) finPage2=true;
                const contents = data.content;
                contents.forEach(content => {
                    const newTr = document.createElement('tr');
                    newTr.id = 'area2'+content.id;

                    let plus = 0;
                    let minus = 0;
                    if(content.typeValue) plus+=content.changingPoint;
                    else minus+=content.changingPoint;
                    [content.regDate.split('T')[0], (plus+'').replace(/\B(?=(\d{3})+(?!\d))/g, ','),
                    (minus+'').replace(/\B(?=(\d{3})+(?!\d))/g, ','), (content.remainingPoint+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')].forEach(text => {
                        const newTd = document.createElement('td');
                        newTd.textContent = text;
                        newTr.appendChild(newTd);
                    });

                    addInsertPoint.appendChild(newTr);
                });
            },
            error: function(xhr) { console.log('error'); }
        });
        page2++;
    });
}

export function getPoint(){
    $.ajax({
        url:'/api/getPoint',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            divId.dataset.number = data.id;
            inputName.value = data.name;
            inputPerPoint.value = data.perPoint;
            inputNotes.value = data.notes;
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function savePoint(){
    const id = Number(divId.dataset.number);
    const point = inputAddPoint.value;
    $.ajax({
        url:'/api/savePoint',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({id:id, changingPoint:point}),
        success:function(data){
            inputAddPoint.value=null;
            getMember();
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}

export function deletePoint(){
    const id = Number(divId.dataset.number);
    const point = inputDeletePoint.value;
    $.ajax({
        url:'/api/deletePoint',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({id:id, changingPoint:point}),
        success:function(data){
            inputDeletePoint.value=null;
            getMember();
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}

export function resetPoint(){
    addInsertPoint.innerHTML='';
}
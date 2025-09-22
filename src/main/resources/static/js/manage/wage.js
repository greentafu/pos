const inputName = document.getElementById('name');
const inputPerWage = document.getElementById('perWage');
const inputNotes = document.getElementById('notes');

export function getWagePage(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/wagePage',
            method:'GET',
            data: {page:page, size:size, searchText:searchText},
            success:function(data){
                totalPages=data.page.totalPages;
                if(totalPages<page) finPage=true;
                const contents = data.content;
                contents.forEach(content => {
                    const newTr = document.createElement('tr');
                    newTr.id = 'area1'+content.id;

                    [content.name, (content.perWage+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원', content.employeeCount+'명'].forEach(text => {
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

export function getWage(){
    $.ajax({
        url:'/api/getWage',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            divId.dataset.number = data.id;
            inputName.value = data.name;
            inputPerWage.value = data.perWage;
            inputNotes.value = data.notes;
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function saveWage(){
    const id = Number(divId.dataset.number);
    const name = inputName.value;
    const perWage = inputPerWage.value;
    const notes = inputNotes.value;
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/saveWage',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({id:id, name:name, perWage:perWage, notes:notes}),
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

export function deleteWage(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/deleteWage',
            method:'DELETE',
            data: {id:selectedRow},
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

export function resetWage(){
    inputName.value=null;
    inputPerWage.value=null;
    inputNotes.value=null;
}
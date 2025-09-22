const inputName = document.getElementById('name');
const inputNotes = document.getElementById('mailNote');
const inputContents = document.getElementById('mailContent');

export function getMailPage(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/mailPage',
            method:'GET',
            data: {page:page, size:size, searchText:searchText},
            success:function(data){
                totalPages=data.page.totalPages;
                if(totalPages<page) finPage=true;
                const contents = data.content;
                contents.forEach(content => {
                    const newTr = document.createElement('tr');
                    newTr.id = 'area1'+content.id;

                    [content.name, content.modDate.split('T')[0], content.notes].forEach(text => {
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

export function getMail(){
    $.ajax({
        url:'/api/getMail',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            divId.dataset.number = data.id;
            inputName.value = data.title;
            inputNotes.value = data.notes;
            inputContents.value = data.content;

            document.getElementById('send_Btn').disabled = false;
            checkByte(inputContents);
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function saveMail(){
    const id = Number(divId.dataset.number);
    const title = inputName.value;
    const notes = inputNotes.value;
    const mailText = inputContents.value;

    return new Promise((resolve) => {
        $.ajax({
            url:'/api/saveMail',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({id:id, title:title, notes:notes, content:mailText}),
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

export function deleteMail(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/deleteMail',
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

export function resetMail(){
    inputName.value=null;
    inputNotes.value=null;
    inputContents.value=null;

    document.getElementById('send_Btn').disabled = true;
    document.getElementById("counter").innerText = '0';
    document.getElementById('mailType').innerText = 'SMS'
}
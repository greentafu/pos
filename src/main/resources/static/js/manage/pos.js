const inputName = document.getElementById('name');
const inputNumber = document.getElementById('number');
const inputLocation = document.getElementById('location');
const inputMachineId = document.getElementById('machineId');
const inputStatus = document.getElementById('status');

export function getPosPage(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/posPage',
            method:'GET',
            data: {page:page, size:size, searchText:searchText},
            success:function(data){
                totalPages=data.page.totalPages;
                if(totalPages<page) finPage=true;
                const contents = data.content;
                contents.forEach(content => {
                    const newTr = document.createElement('tr');
                    newTr.id = 'area1'+content.id;

                    [content.number, content.name, content.location, content.status==true? "활성" : "비활성"].forEach(text => {
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

export function getPos(){
    $.ajax({
        url:'/api/getPos',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            divId.dataset.number = data.id;
            inputName.value = data.name;
            inputNumber.value = data.number;
            inputLocation.value = data.location;
            inputMachineId.value = data.machineId;
            inputStatus.checked = data.status;
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function savePos(){
    const id = Number(divId.dataset.number);
    const name = inputName.value;
    const number = inputNumber.value;
    const location = inputLocation.value;
    const machineId = inputMachineId.value;
    const status = inputStatus.checked;

    return new Promise((resolve) => {
        $.ajax({
            url:'/api/savePos',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({id:id, number:number, name:name, machineId:machineId, location:location, status:status}),
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

export function deletePos(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/deletePos',
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

export function resetPos(){
    inputName.value=null;
    inputNumber.value=null;
    inputLocation.value=null;
    inputMachineId.value=null;
    inputStatus.checked=false;
}
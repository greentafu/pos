const inputName = document.getElementById('name');
const selectWage = document.getElementById('perWage');

export function getPositionPage(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/jobTitlePage',
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

export function getPosition(){
    $.ajax({
        url:'/api/getJobTitle',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            divId.dataset.number = data.jobTitleDTO.id;
            inputName.value = data.jobTitleDTO.name;
            selectWage.value = data.jobTitleDTO.wageDTO.id;
            const screenAuthList = data.screenAuthorityDTOList;
            screenAuthList.forEach(aut => {
                const authority = aut.authority;
                const screenID = aut.screenDTO.id;
                document.getElementById('screen'+screenID).checked = authority;
            });
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function savePosition(){
    const id = Number(divId.dataset.number);
    const name = inputName.value;
    const wage = Number(selectWage.value);
    const screenAuthList = [];

    for(let i=2; i<14; i++){
        if(i!==6){
            const flag = document.getElementById('screen'+i).checked;
            screenAuthList.push(flag);
        }
    }

    return new Promise((resolve) => {
        $.ajax({
            url:'/api/saveJobTitle',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({id:id, name:name, wage:wage, screenAuthList:screenAuthList}),
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

export function deletePosition(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/deleteJobTitle',
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

export function resetPosition(){
    inputName.value=null;
    selectWage.selectedIndex = 0;
    const allSwitch = document.querySelectorAll('.switch');
    allSwitch.forEach(swi => swi.checked=true);
}
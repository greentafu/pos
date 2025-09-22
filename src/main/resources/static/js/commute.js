function debounce(fn, delay) {
    let timer;
    return function (...args) {
        clearTimeout(timer);
        timer = setTimeout(() => fn.apply(this, args), delay);
    };
}

document.addEventListener("DOMContentLoaded", () => {
    scrollStep = scrollArea.clientHeight-60;
    const listHeight = scrollArea.offsetHeight-35;
    size = Math.floor(listHeight/30)*2;

    getPage();
    getSideList();
    showScrollArea();
    showScrollArea2();
    clickRow();
    clickRow2();
    clickRow3();

    searchInput.addEventListener('input', debounce(() => {
        searchText = searchInput.value;
        defaultInsertPoint.innerHTML = '';
        page=1;
        finPage=false;
        getPage();
    }, 300));
});

// page가져오기
function getPage(){
    $.ajax({
        url:'/api/commutePage',
        method:'GET',
        data: {page:page, size:size, searchText:searchText},
        success:function(data){
            totalPages=data.page.totalPages;
            if(totalPages<page) finPage=true;
            const contents = data.content;
            contents.forEach(content => {
                const newTr = document.createElement('tr');
                newTr.id = 'area1'+content.id;
                if(Number(selectedRow)===Number(content.id)) newTr.style.backgroundColor = '#81ACEC';

                const startTime = (content.startTime)?content.startTime:'';
                const endTime = (content.endTime)?content.endTime:'';
                const notes = (content.notes)?content.notes:'';
                [content.number, content.name, content.jobTitle, startTime, endTime, notes].forEach(text => {
                    const newTd = document.createElement('td');
                    newTd.textContent = text;
                    newTr.appendChild(newTd);
                });

                defaultInsertPoint.appendChild(newTr);
            });
            page++;
            updateButtonColors();
        },
        error: function(xhr) { console.log('error'); }
    });
}
function getSideList(){
    document.getElementById('workingCount').textContent = '0명';
    thirdInsertPoint.textContent='';
    $.ajax({
        url:'/api/workingEmployee',
        method:'GET',
        data: {page:page, size:size, searchText:searchText},
        success:function(data){
            if(data==='') return null;

            document.getElementById('workingCount').textContent = data.length+'명';
            data.forEach(dto => {
                const newTr = document.createElement('tr');
                newTr.id = 'working'+dto.id;
                if(Number(selectedRow3)===Number(dto.id)) newTr.style.backgroundColor = '#81ACEC';

                [dto.number, dto.name].forEach(text => {
                    const newTd = document.createElement('td');
                    newTd.textContent = text;
                    newTr.appendChild(newTd);
                });

                thirdInsertPoint.appendChild(newTr);
            });
        },
        error: function(xhr) { console.log('error'); }
    });
}
function getPage2(){
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    $.ajax({
        url:'/api/commuteDetailPage',
        method:'GET',
        data: {page:page2, size:size2, startDate:startDate, endDate:endDate, id:selectedRow},
        success:function(data){
            totalPages2=data.page.totalPages;
            if(totalPages2<page2) finPage2=true;
            const contents = data.content;
            contents.forEach(content => {
                const newTr = document.createElement('tr');
                newTr.id = 'area2'+content.id;

                const startTime = (content.startTime)?content.startTime:'';
                const endTime = (content.endTime)?content.endTime:'';
                const notes = (content.notes)?content.notes:'';
                [content.number, content.name, content.jobTitle, startTime, endTime, notes].forEach(text => {
                    const newTd = document.createElement('td');
                    newTd.textContent = text;
                    newTr.appendChild(newTd);
                });

                addInsertPoint.appendChild(newTr);
            });
            page2++;
            updateButtonColors2();
        },
        error: function(xhr) { console.log('error'); }
    });
}
function search(){
    addInsertPoint.textContent='';
    page2=1;
    finPage2=false;
    getPage2();
}

// set/reset
function setSelectEmployee(id){
    $.ajax({
        url:'/api/getCommuteEmployee',
        method:'GET',
        data: {id:id},
        success:function(data){
            document.getElementById('selectedName').textContent = data.name;
            if(data.startTime!==null && data.endTime===null){
                document.getElementById('employeePassword').value='';
                document.getElementById('goWork').style.display = 'none';
                document.getElementById('getOffWork').style.display = '';
            }else{
                document.getElementById('employeePassword').style.display = '';
                document.getElementById('goWork').style.display = '';
                document.getElementById('getOffWork').style.display = 'none';
            }
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}
function resetSelectEmployee(){
    const nameDiv = document.getElementById('selectedName');
    const passwordInput = document.getElementById('employeePassword');
    const employeePhone = document.getElementById('employeePhone');
    const selectedTel = document.getElementById('selectedTel');
    const goWork = document.getElementById('goWork');
    const getOffWork = document.getElementById('getOffWork');

    passwordInput.style.display = '';
    employeePhone.style.display = 'none';
    goWork.style.display = '';
    getOffWork.style.display = 'none';

    nameDiv.textContent = '';
    passwordInput.value = '';
    selectedTel.textContent = '';
}
function setCommuteTime(){
    $.ajax({
        url:'/api/getCommuteTime',
        method:'GET',
        data: {id:selectedRow2},
        success:function(data){
            console.log(data);

            const start = formatDateTime(data.startTime);
            const end = formatDateTime(data.endTime);

            document.getElementById('beforeGoWork').textContent=start;
            document.getElementById('beforeGetOffWork').textContent=end;

            document.getElementById('startH').value=start.split(':')[0];
            document.getElementById('startM').value=start.split(':')[1];
            document.getElementById('endH').value=end.split(':')[0];
            document.getElementById('endM').value=end.split(':')[1];
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");

            selectedRow2='';
            resetCommuteTime();
            const rows = scrollArea2.querySelectorAll('table.management tbody tr');
            rows.forEach(temp => temp.style.backgroundColor = '');
        }
    });
}
function resetCommuteTime(){
    document.getElementById('beforeGoWork').textContent='';
    document.getElementById('beforeGetOffWork').textContent='';
    document.getElementById('startH').value='';
    document.getElementById('startM').value='';
    document.getElementById('endH').value='';
    document.getElementById('endM').value='';
}
function formatDateTime(tempDate) {
    if(tempDate===null) return '00:00';
    const pad = n => n.toString().padStart(2, '0');

    const date = new Date(tempDate);
    const HH = pad(date.getHours());
    const mm = pad(date.getMinutes());
    return `${HH}:${mm}`;
}

// go/getOff
function goWork(){
    const passWord = document.getElementById('employeePassword').value;
    $.ajax({
        url:'/api/saveCommuteStart',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({id:Number(selectedRow), userPw:passWord}),
        success:function(data){
            defaultInsertPoint.innerHTML = '';
            page=1;
            finPage=false;
            getPage();
            getSideList();

            selectedRow='';
            selectedRow3='';
            resetSelectEmployee();
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}
function getOffWork(){
    const passWord = document.getElementById('employeePassword').value;
    $.ajax({
        url:'/api/saveCommuteEnd',
        method:'PUT',
        contentType: "application/json",
        data: JSON.stringify({id:Number(selectedRow), userPw:passWord}),
        success:function(data){
            defaultInsertPoint.innerHTML = '';
            page=1;
            finPage=false;
            getPage();
            getSideList();

            selectedRow='';
            selectedRow3='';
            resetSelectEmployee();
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}

// updateTime
function limitHour(input){
    const pad = n => n.toString().padStart(2, '0');

    let numbers = input.value.replace(/\D/g, '');
    numbers = String(parseInt(numbers, 10) || 0);
    if(numbers<0) numbers=0;
    else if(numbers>23) numbers=23;
    input.value = pad(numbers);
}
function limitMinute(input){
    const pad = n => n.toString().padStart(2, '0');

    let numbers = input.value.replace(/\D/g, '');
    numbers = String(parseInt(numbers, 10) || 0);
    if(numbers<0) numbers=0;
    else if(numbers>59) numbers=59;
    input.value = pad(numbers);
}
function updateCommuteTime(){
    const startHour = document.getElementById('startH').value;
    const startMinute = document.getElementById('startM').value;
    const endHour = document.getElementById('endH').value;
    const endMinute = document.getElementById('endM').value;

    $.ajax({
        url:'/api/updateCommuteTime',
        method:'PUT',
        contentType: "application/json",
        data: JSON.stringify({id:Number(selectedRow2), startHour:startHour, startMinute:startMinute,
                            endHour:endHour, endMinute:endMinute}),
        success:function(data){
            selectedRow2='';
            resetCommuteTime();

            addInsertPoint.innerHTML = '';
            page2=1;
            finPage2=false;
            getPage2();

            defaultInsertPoint.innerHTML = '';
            page=1;
            finPage=false;
            getPage();
            getSideList();
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}

// clickRow
function clickRow(){
    scrollArea.addEventListener("click", (event) => {
        const rows = scrollArea.querySelectorAll('table.management tbody tr');
        const row = event.target.closest('tbody tr');

        const rows3 = scrollArea3.querySelectorAll('table.management tbody tr');

        if(row!==null) {
            const color = row.style.backgroundColor;
            if(color==='') {
                rows.forEach(temp => temp.style.backgroundColor = '');
                rows3.forEach(temp => temp.style.backgroundColor = '');
                row.style.backgroundColor = '#81ACEC';
                selectedRow=row.id.slice(5);
                selectedRow3=row.id.slice(5);

                const worker = document.getElementById('working'+selectedRow3);
                if(worker) worker.style.backgroundColor = '#81ACEC';

                setSelectEmployee(selectedRow);
            }else{
                row.style.backgroundColor = '';
                const worker = document.getElementById('working'+selectedRow3);
                if(worker) worker.style.backgroundColor = '';
                selectedRow= '';
                selectedRow3= '';
                resetSelectEmployee();
            }
        }
    });
}
function clickRow2(){
    scrollArea2.addEventListener("click", (event) => {
        const rows = scrollArea2.querySelectorAll('table.management tbody tr');
        const row = event.target.closest('tbody tr');
        if(row!==null) {
            const color = row.style.backgroundColor;
            if(color==='') {
                rows.forEach(temp => temp.style.backgroundColor = '');
                row.style.backgroundColor = '#81ACEC';
                selectedRow2=row.id.slice(5);
                setCommuteTime();
            }else {
                row.style.backgroundColor = '';
                selectedRow2='';
                resetCommuteTime();
            }
        }
    });
}
function clickRow3(){
    scrollArea3.addEventListener("click", (event) => {
        const rows = scrollArea3.querySelectorAll('table.management tbody tr');
        const row = event.target.closest('tbody tr');

        const rows1 = scrollArea.querySelectorAll('table.management tbody tr');

        if(row!==null) {
            const color = row.style.backgroundColor;
            if(color==='') {
                rows.forEach(temp => temp.style.backgroundColor = '');
                rows1.forEach(temp => temp.style.backgroundColor = '');
                row.style.backgroundColor = '#81ACEC';
                selectedRow3=row.id.slice(7);
                selectedRow=row.id.slice(7);

                const worker = document.getElementById('area1'+selectedRow);
                if(worker) worker.style.backgroundColor = '#81ACEC';

                setSelectEmployee(selectedRow);
            }else{
                row.style.backgroundColor = '';
                const worker = document.getElementById('area1'+selectedRow);
                if(worker) worker.style.backgroundColor = '#81ACEC';
                selectedRow3= '';
                selectedRow= '';
                resetSelectEmployee();
            }
        }
    });
}

// default/addPage
function openAddPage(){
    defaultPage.style.display='none';
    addPage.style.display='';

    scrollStep2 = scrollArea2.clientHeight-60;
    const listHeight = scrollArea2.offsetHeight-35;
    size2 = Math.floor(listHeight/30)*2;

    page2=1;
    finPage2=false;
}
function closeAddPage(){
    addPage.style.display='none';
    defaultPage.style.display='';

    addInsertPoint.textContent='';
    const todayRadio = document.getElementById('todayRadio');
    todayRadio.checked = true;
    todayRadio.dispatchEvent(new Event("change"));
    selectedRow2='';
    resetCommuteTime();
}
document.addEventListener("DOMContentLoaded", () => {
    scrollStep = scrollArea.clientHeight-60;
    const listHeight = scrollArea.offsetHeight-35;
    size = Math.floor(listHeight/30)*2;
    size2 = Math.floor(listHeight/30)*2;

    getPage();
    getPage2();
    showScrollArea();
    showScrollArea2();
    clickRow();
    clickRow2();
});

// page가져오기
function getPage(){
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
    const searchText = document.getElementById('searchName').value;
    const searchCategory = document.getElementById('perWage').value;

    $.ajax({
        url:'/api/paymentPage',
        method:'GET',
        data: {page:page, size:size, startDate:startDate, endDate:endDate, searchText:searchText, searchCategory:searchCategory},
        success:function(data){
            totalPages=data.page.totalPages;
            if(totalPages<page) finPage=true;

            const contents = data.content;
            contents.forEach(content => {
                const newTr = document.createElement('tr');
                newTr.id = 'area1'+content.id;
                if(Number(selectedRow)===Number(content.id)) newTr.style.backgroundColor = '#81ACEC';

                const date = formatDateTime(content.startTime, 0);
                const start = formatDateTime(content.startTime, 1);
                const end = formatDateTime(content.endTime, 1);
                const diff = differenceTwoDate(start, end);
                const wage = Math.floor(diff*content.perWage/60);
                [date, content.number, content.name, content.wageName, start, end,
                formatHourMinute(diff, 0), (wage+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원'].forEach(text => {
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
function getPage2(){
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
    const searchText = document.getElementById('searchName').value;
    const searchCategory = document.getElementById('perWage').value;

    $.ajax({
        url:'/api/paymentSimplePage',
        method:'GET',
        data: {page:page2, size:size2, startDate:startDate, endDate:endDate, searchText:searchText, searchCategory:searchCategory},
        success:function(data){
            totalPages2=data.page.totalPages;
            if(totalPages2<page2) finPage2=true;

            dayjs.extend(window.dayjs_plugin_isSameOrBefore);
            const jsStartDate = dayjs(formatDateTime(startDate, 0));
            const jsEndDate = dayjs(formatDateTime(endDate, 0));

            const headTr = document.getElementById('headTr');
            const headThs = headTr.querySelectorAll('th');
            if(headThs.length===6){
                let current = jsStartDate;
                while (current.isSameOrBefore(endDate)) {
                    const newTh = document.createElement('th');
                    newTh.textContent = current.format('MM/DD');
                    newTh.style.minWidth = '50px';
                    current = current.add(1, 'day');
                    headTr.appendChild(newTh);
                }
            }

            const contents = data.content;
            contents.forEach(content => {
                const newTr = document.createElement('tr');
                newTr.id = 'area2'+content.id;
                if(Number(selectedRow)===Number(content.id)) newTr.style.backgroundColor = '#81ACEC';

                const timeMap = new Map(Object.entries(content.timeMap));

                let allDay=0;
                let allTime=0;
                let allWage=0;
                timeMap.forEach((value, key) => {
                    allDay++;
                    allTime+=value.time;
                    allWage+=value.wage;
                });

                [content.number, content.name, (allWage+'').replace(/\B(?=(\d{3})+(?!\d))/g, ','), startDate+'~'+endDate,
                (allDay+'').replace(/\B(?=(\d{3})+(?!\d))/g, ','), formatHourMinute(allTime, 1)].forEach(text => {
                    const newTd = document.createElement('td');
                    newTd.textContent = text;
                    newTr.appendChild(newTd);
                });

                let current = jsStartDate;
                while (current.isSameOrBefore(endDate)) {
                    const newTd = document.createElement('td');
                    const tempKey = current.format('MM/DD');
                    if(timeMap.has(tempKey)) {
                        const tempMinute = timeMap.get(tempKey).time;
                        newTd.textContent = formatHourMinute(tempMinute, 1);
                    }
                    current = current.add(1, 'day');
                    newTr.appendChild(newTd);
                }

                addInsertPoint.appendChild(newTr);
            });
            page2++;
            updateButtonColors2();
        },
        error: function(xhr) { console.log('error'); }
    });
}

// formatTime
function formatDateTime(tempDate, type) {
    const pad = n => n.toString().padStart(2, '0');
    const date = new Date(tempDate);
    const yyyy = date.getFullYear();
    const MM = pad(date.getMonth() + 1);
    const dd = pad(date.getDate());
    const HH = pad(date.getHours());
    const mm = pad(date.getMinutes());
    const ss = pad(date.getSeconds());
    if(type===0) return `${yyyy}-${MM}-${dd}`;
    return `${yyyy}-${MM}-${dd} ${HH}:${mm}:${ss}`;
}
function differenceTwoDate(tempStart, tempEnd) {
    const start = dayjs(tempStart, "YYYY-MM-DD HH:mm");
    const end = dayjs(tempEnd, "YYYY-MM-DD HH:mm");
    return end.diff(start, 'minute');
}
function formatHourMinute(totalMinutes, type) {
    const pad = n => n.toString().padStart(2, '0');
    let hours = Math.floor(totalMinutes / 60);
    if((hours+'').length<2) hours = pad(hours);
    const minutes = pad(totalMinutes % 60);

    if(type===0) return `${hours}시간 ${minutes}분`;
    return `${hours}:${minutes}`;
}

// search
function search(){
    defaultInsertPoint.textContent='';
    addInsertPoint.textContent='';
    const headTr = document.getElementById('headTr');
    headTr.querySelectorAll('th').forEach(th => {
        if(!th.classList.contains('fixTh')) th.remove();
    });
    page=1;
    page2=1;
    finPage=false;
    finPage2=false;
    getPage();
    getPage2();
}
function showDetail(){
    defaultPage.style.display='';
    addPage.style.display='none';
}
function showSimple(){
    defaultPage.style.display='none';
    addPage.style.display='';
}

// clickRow
function clickRow(){
    scrollArea.addEventListener("click", (event) => {
        const rows = scrollArea.querySelectorAll('table.history tbody tr');
        const row = event.target.closest('tbody tr');

        if(row!==null) {
            const color = row.style.backgroundColor;
            if(color==='') {
                rows.forEach(temp => temp.style.backgroundColor = '');
                row.style.backgroundColor = '#81ACEC';
                selectedRow=row.id.slice(5);
            }else{
                row.style.backgroundColor = '';
                selectedRow= '';
            }
        }
    });
}
function clickRow2(){
    scrollArea2.addEventListener("click", (event) => {
        const rows = scrollArea2.querySelectorAll('table.history tbody tr');
        const row = event.target.closest('tbody tr');

        if(row!==null) {
            const color = row.style.backgroundColor;
            if(color==='') {
                rows.forEach(temp => temp.style.backgroundColor = '');
                row.style.backgroundColor = '#81ACEC';
                selectedRow2=row.id.slice(5);
            }else{
                row.style.backgroundColor = '';
                selectedRow2= '';
            }
        }
    });
}
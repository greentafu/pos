document.addEventListener("DOMContentLoaded", () => {
    // 화면time
    const posDate = document.getElementById('posDate');
    const posTime = document.getElementById('posTime');
    if(posDate && posTime){
        updateHeaderTime();
        setInterval(updateHeaderTime, 1000);
    }

    // input date
    const dateInputs = document.querySelectorAll('input[type=date]');
    const monthInput = document.getElementById('selectedMonth');
    if(monthInput){
        const today = new Date();
        const formatDateLocal = (date) => {
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            return `${year}-${month}`;
        };
        monthInput.value=formatDateLocal(today);
    }
    if(dateInputs.length>0){
        const startDate = document.getElementById('startDate');
        const endDate = document.getElementById('endDate');
        const todayRadio = document.getElementById('todayRadio');
        const weekRadio = document.getElementById('weekRadio');
        const monthRadio = document.getElementById('monthRadio');
        const dateRadios = [todayRadio, weekRadio, monthRadio];

        // 날짜
        const today = new Date();

        const formatDateLocal = (date) => {
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            return `${year}-${month}-${day}`;
        };

        const startOfWeek = new Date(today);
        const endOfWeek = new Date(today);
        startOfWeek.setDate(today.getDate() - today.getDay());
        endOfWeek.setDate(today.getDate() + (6 - today.getDay()));

        const startOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
        const endOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0);

        const format_today = formatDateLocal(today);
        const format_startWeek = formatDateLocal(startOfWeek);
        const format_endWeek = formatDateLocal(endOfWeek);
        const format_startMonth = formatDateLocal(startOfMonth);
        const format_endMonth = formatDateLocal(endOfMonth);

        // 기본 동작
        startDate.value = format_today;
        endDate.value = format_today;
        todayRadio.checked = true;

        // input 변경
        dateInputs.forEach(dateInput => {
            dateInput.addEventListener('change', function(){
                if(startDate.value===format_today && endDate.value===format_today){
                    todayRadio.checked = true;
                }else if(startDate.value===format_startWeek && endDate.value===format_endWeek){
                    weekRadio.checked = true;
                }else if(startDate.value===format_startMonth && endDate.value===format_endMonth){
                    monthRadio.checked = true;
                }else{
                    dateRadios.forEach(dateRadio => dateRadio.checked = false);
                }
            });
        });
        // radio 변경
        dateRadios.forEach(dateRadio => {
            dateRadio.addEventListener('change', function(){
                if(dateRadio===todayRadio){
                    startDate.value = format_today;
                    endDate.value = format_today;
                }else if(dateRadio===weekRadio){
                    startDate.value = format_startWeek;
                    endDate.value = format_endWeek;
                }else if(dateRadio===monthRadio){
                    startDate.value = format_startMonth;
                    endDate.value = format_endMonth;
                }
            });
        });
    }
});

function updateHeaderTime() {
    const now = new Date();

    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const date = String(now.getDate()).padStart(2, '0');

    const dayNames = ['일', '월', '화', '수', '목', '금', '토'];
    const day = dayNames[now.getDay()];

    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');

    const today = `${year}-${month}-${date}(${day})`;
    const time = `${hours}:${minutes}`;
    posDate.textContent = today;
    posTime.textContent = time;

    const commuteDate = document.getElementById('commuteDate');
    const commuteDate2 = document.getElementById('commuteDate2');
    if(commuteDate) commuteDate.textContent = today;
    if(commuteDate2) commuteDate2.textContent = today;
}
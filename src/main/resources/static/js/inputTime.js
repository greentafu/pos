const inputTimes=document.querySelectorAll('.inputTimes');
inputTimes.forEach(inputTime => {
    const startHour=inputTime.querySelector('.startHour');
    const endHour=inputTime.querySelector('.endHour');
    const startMin=inputTime.querySelector('.startMin');
    const endMin=inputTime.querySelector('.endMin');

    const hours=[startHour, endHour];
    const minutes=[startMin, endMin];

    hours.forEach(hour => {
        hour.addEventListener('input', function(){
            hour.value = hour.value.replace(/\D/g, "");
            if (hour.value.length > 2) hour.value = hour.value.slice(-2);
        });
        hour.addEventListener("blur", () => {
            if (startHour.value.length === 1) startHour.value = startHour.value.padStart(2, "0");
            else if (startHour.value === "") startHour.value = "00";

            if (endHour.value.length === 1) endHour.value = endHour.value.padStart(2, "0");
            else if (endHour.value === "") endHour.value = "00";

            const startH=parseInt(startHour.value, 10);
            const endH=parseInt(endHour.value, 10);
            const startM=parseInt(startMin.value, 10);
            const endM=parseInt(endMin.value, 10);

            if(!isNaN(startH)){
                if (startH > 23) startHour.value = 23;
                else if (startH < 0) startHour.value = 0;
            }
            if(!isNaN(endH)){
                if (endH > 23) endHour.value = 23;
                else if (endH < 0) endHour.value = 0;
            }
            if(startH>endH) endHour.value = startHour.value;
            if(startH === endH && startM > endM) endMin.value=startM;
        });
    });
            
    minutes.forEach(minute => {
        minute.addEventListener('input', function(){
            minute.value = minute.value.replace(/\D/g, "");
            if (minute.value.length > 2) minute.value = minute.value.slice(-2);
        });
        minute.addEventListener("blur", () => {
            if (startMin.value.length === 1) startMin.value = startMin.value.padStart(2, "0");
            else if (startMin.value === "") startMin.value = "00";

            if (endMin.value.length === 1) endMin.value = endMin.value.padStart(2, "0");
            else if (endMin.value === "") endMin.value = "00";

            const startH=parseInt(startHour.value, 10);
            const endH=parseInt(endHour.value, 10);
            const startM=parseInt(startMin.value, 10);
            const endM=parseInt(endMin.value, 10);

            if(!isNaN(startM)){
                if (startM > 59) startMin.value = 59;
                else if (startM < 0) startMin.value = 0;
            }
            if(!isNaN(endM)){
                if (endM > 59) endMin.value = 59;
                else if (endM < 0) endMin.value = 0;
            }
            if(startH === endH && startM > endM) endMin.value=startM;
        });
    });
});
let selectedInput='';
const numBtns = document.querySelectorAll('[id^="numBtn"]');

document.addEventListener('mousedown', (e) => {
    if(e.target === selectedInput || e.target.closest('[id^="numBtn"]')) e.preventDefault();
    else if(e.target.matches('[id^="numBtn"]')) return null;
    else if(e.target.matches('input')) selectedInput=e.target;
    else {
        selectedInput='';
        document.activeElement.blur();
    }
    if(selectedInput!=='') selectedInput.focus();
});

numBtns.forEach(numBtn => {
    numBtn.addEventListener('click', ()=>{
        if(selectedInput!==''){
            const btnId=numBtn.id;
            const content=btnId.slice(6);
            let inputSize=selectedInput.value.length;

            if(selectedInput.classList.contains('phoneNumber')){
                if(content==='C') selectedInput.value='';
                else if(content==='Delete') selectedInput.value=selectedInput.value.slice(0, -1);
                else if(content==='Enter') selectedInput.blur();
                else {
                    if(inputSize<13) selectedInput.value+=content;
                    inputSize=selectedInput.value.length;
                    if(inputSize>13) selectedInput.value=selectedInput.value.slice(0, 13);
                }
                formatPhoneNumber(selectedInput);
            }else if(selectedInput.classList.contains('autoComma')){
                if(content==='C') selectedInput.value='';
                else if(content==='Delete') selectedInput.value=selectedInput.value.slice(0, -1);
                else if(content==='Enter') selectedInput.blur();
                else selectedInput.value+=content;
                formatAutoComma(selectedInput);
            }
            else {
                if(content==='C') selectedInput.value='';
                else if(content==='Delete') selectedInput.value=selectedInput.value.slice(0, -1);
                else if(content==='Enter') selectedInput.blur();
                else selectedInput.value +=content;
            }
            selectedInput.dispatchEvent(new Event('input'));
        }
    });
});

function formatAutoComma(input) {
    if(selectedInput!==null && selectedInput!==''){
        let numbers = selectedInput.value.replace(/\D/g, '');
        numbers = String(parseInt(numbers, 10) || 0);
        input.value = numbers.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }
}
function formatAutoCommaLimit(input, limit) {
    if(selectedInput!==null && selectedInput!==''){
        let numbers = selectedInput.value.replace(/\D/g, '');
        numbers = String(parseInt(numbers, 10) || 0);
        if(numbers>limit) numbers = limit;
        input.value = (numbers+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }
}

function formatComma(temp) {
    let tempString = temp+'';
    if(tempString.includes('.')) tempString = tempString.split('.')[0];
    let numbers = tempString.replace(/\D/g, '');
    numbers = String(parseInt(numbers, 10) || 0);
    return numbers.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

function formatPhoneNumber(input) {
    let numbers = input.value.replace(/\D/g, '');
    if (numbers.length <= 3) {
        input.value = numbers;
    } else if (numbers.length <= 7) {
        input.value = numbers.slice(0, 3) + '-' + numbers.slice(3);
    } else if (numbers.length <= 11) {
        input.value = numbers.slice(0, 3) + '-' + numbers.slice(3, 7) + '-' + numbers.slice(7);
    }
}

function onlyNumber(input){
    input.value = input.value.replace(/\D/g, '');
}
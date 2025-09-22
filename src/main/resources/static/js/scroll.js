let pageName='';

const defaultPage = document.getElementById('defaultPage');
const defaultInsertPoint = document.getElementById('defaultInsertPoint');
const searchInput = document.getElementById('searchInput');
const searchSelect = document.getElementById('searchSelect');
const scrollArea = document.getElementById("scrollArea");
const scrollUpBtn = document.getElementById("scrollUp");
const scrollDownBtn = document.getElementById("scrollDown");
let scrollStep;
let page=1;
let size=20;
let totalPages=0;
let finPage=false;
let selectedRow='';
let searchText='';
let searchCategory=null;

const addPage = document.getElementById('addPage');
const addInsertPoint = document.getElementById('addInsertPoint');
const searchInput2 = document.getElementById('searchInput2');
const searchSelect2 = document.getElementById('searchSelect2');
const scrollArea2 = document.getElementById("scrollArea2");
const scrollUpBtn2 = document.getElementById("scrollUp2");
const scrollDownBtn2 = document.getElementById("scrollDown2");
let scrollStep2;
let page2=1;
let size2=20;
let totalPages2=0;
let finPage2=false;
let selectedRow2='';
let searchText2='';
let searchCategory2=null;

const thirdPage = document.getElementById('thirdPage');
const thirdInsertPoint = document.getElementById('thirdInsertPoint');
const scrollArea3 = document.getElementById("scrollArea3");
const scrollUpBtn3 = document.getElementById("scrollUp3");
const scrollDownBtn3 = document.getElementById("scrollDown3");
let scrollStep3;
let page3=1;
let size3=20;
let totalPages3=0;
let finPage3=false;
let selectedRow3='';

const fourthPage = document.getElementById('fourthPage');
const fourthInsertPoint = document.getElementById('fourthInsertPoint');
const scrollArea4 = document.getElementById("scrollArea4");
const scrollUpBtn4 = document.getElementById("scrollUp4");
const scrollDownBtn4 = document.getElementById("scrollDown4");
let scrollStep4;
let page4=1;
let size4=20;
let totalPages4=0;
let finPage4=false;
let selectedRow4='';

const fifthPage = document.getElementById('fifthPage');
const fifthInsertPoint = document.getElementById('fifthInsertPoint');
const scrollArea5 = document.getElementById("scrollArea5");
const scrollUpBtn5 = document.getElementById("scrollUp5");
const scrollDownBtn5 = document.getElementById("scrollDown5");
let scrollStep5;
let page5=1;
let size5=20;
let totalPages5=0;
let finPage5=false;
let selectedRow5='';

const EPSILON = 2;
let lastScrollTop = 0;
let lastScrollTop2 = 0;
let lastScrollTop3 = 0;
let lastScrollTop4 = 0;
let lastScrollTop5 = 0;

let pageList = [page, page2, page3, page4, page5];
let sizeList = [size, size2, size3, size4, size5];
let totalPageList = [totalPages, totalPages2, totalPages3, totalPages4, totalPages5];
let finPageList = [finPage, finPage2, finPage3, finPage4, finPage5];
let scrollStepList = [scrollStep, scrollStep2, scrollStep3, scrollStep4, scrollStep5];
let lastScrollTopList = [lastScrollTop, lastScrollTop2, lastScrollTop3, lastScrollTop4, lastScrollTop5];

function showScrollArea(){
    updateButtonColors();

    scrollUpBtn.addEventListener("click", () => {
        scrollArea.scrollTop -= scrollStep;
        updateButtonColors();

        const currentScrollTop = scrollArea.scrollTop;
        lastScrollTop = currentScrollTop;
    });
    scrollDownBtn.addEventListener("click", () => {
        const isAtBottom = scrollArea.scrollTop + scrollArea.clientHeight >= scrollArea.scrollHeight - 25;
        if (isAtBottom) {
            getPage().then(() => {
                scrollArea.scrollTop += scrollStep;
                updateButtonColors();
            });
        }else{
            scrollArea.scrollTop += scrollStep;
            updateButtonColors();
        }

        const currentScrollTop = scrollArea.scrollTop;
        lastScrollTop = currentScrollTop;
    });

    scrollArea.addEventListener("scroll", () => {
        const currentScrollTop = scrollArea.scrollTop;

        if (currentScrollTop > lastScrollTop) {
            const isAtBottom = scrollArea.scrollTop + scrollArea.clientHeight >= scrollArea.scrollHeight - 25;
            if (isAtBottom) getPage();
        }
        updateButtonColors();
        lastScrollTop = currentScrollTop;
    });
}
function showScrollArea2(){
    updateButtonColors2();

    scrollUpBtn2.addEventListener("click", () => {
        scrollArea2.scrollTop -= scrollStep2;
        updateButtonColors2();

        const currentScrollTop = scrollArea2.scrollTop;
        lastScrollTop2 = currentScrollTop;
    });
    scrollDownBtn2.addEventListener("click", () => {
        const isAtBottom = scrollArea2.scrollTop + scrollArea2.clientHeight >= scrollArea2.scrollHeight - 25;
        if (isAtBottom) {
            getPage2().then(() => {
                scrollArea2.scrollTop += scrollStep2;
                updateButtonColors2();
            });
        }else{
            scrollArea2.scrollTop += scrollStep2;
            updateButtonColors2();
        }

        const currentScrollTop = scrollArea2.scrollTop;
        lastScrollTop2 = currentScrollTop;
    });

    scrollArea2.addEventListener("scroll", () => {
        const currentScrollTop = scrollArea2.scrollTop;

        if (currentScrollTop > lastScrollTop2) {
            const isAtBottom = scrollArea2.scrollTop + scrollArea2.clientHeight >= scrollArea2.scrollHeight - 25;
            if (isAtBottom) getPage2();
        }
        updateButtonColors2();
        lastScrollTop2 = currentScrollTop;
    });
}
function showScrollArea3(){
    updateButtonColors3();

    scrollUpBtn3.addEventListener("click", () => {
        scrollArea3.scrollTop -= scrollStep3;
        updateButtonColors3();

        const currentScrollTop = scrollArea3.scrollTop;
        lastScrollTop3 = currentScrollTop;
    });
    scrollDownBtn3.addEventListener("click", () => {
        const isAtBottom = scrollArea3.scrollTop + scrollArea3.clientHeight >= scrollArea3.scrollHeight - 25;
        if (isAtBottom) {
            getPage3().then(() => {
                scrollArea3.scrollTop += scrollStep3;
                updateButtonColors3();
            });
        }else{
            scrollArea3.scrollTop += scrollStep3;
            updateButtonColors3();
        }

        const currentScrollTop = scrollArea3.scrollTop;
        lastScrollTop3 = currentScrollTop;
    });

    scrollArea3.addEventListener("scroll", () => {
        const currentScrollTop = scrollArea3.scrollTop;

        if (currentScrollTop > lastScrollTop3) {
            const isAtBottom = scrollArea3.scrollTop + scrollArea3.clientHeight >= scrollArea3.scrollHeight - 25;
            if (isAtBottom) getPage3();
        }
        updateButtonColors3();
        lastScrollTop3 = currentScrollTop;
    });
}
function showScrollArea4(){
    updateButtonColors4();

    scrollUpBtn4.addEventListener("click", () => {
        scrollArea4.scrollTop -= scrollStep4;
        updateButtonColors4();

        const currentScrollTop = scrollArea4.scrollTop;
        lastScrollTop4 = currentScrollTop;
    });
    scrollDownBtn4.addEventListener("click", () => {
        const isAtBottom = scrollArea4.scrollTop + scrollArea4.clientHeight >= scrollArea4.scrollHeight - 25;
        if (isAtBottom) {
            getPage4().then(() => {
                scrollArea4.scrollTop += scrollStep4;
                updateButtonColors4();
            });
        }else{
            scrollArea4.scrollTop += scrollStep4;
            updateButtonColors4();
        }

        const currentScrollTop = scrollArea4.scrollTop;
        lastScrollTop4 = currentScrollTop;
    });

    scrollArea4.addEventListener("scroll", () => {
        const currentScrollTop = scrollArea4.scrollTop;

        if (currentScrollTop > lastScrollTop4) {
            const isAtBottom = scrollArea4.scrollTop + scrollArea4.clientHeight >= scrollArea4.scrollHeight - 25;
            if (isAtBottom) getPage4();
        }
        updateButtonColors4();
        lastScrollTop4 = currentScrollTop;
    });
}
function showScrollArea5(){
    updateButtonColors5();

    scrollUpBtn5.addEventListener("click", () => {
        scrollArea5.scrollTop -= scrollStep5;
        updateButtonColors5();

        const currentScrollTop = scrollArea5.scrollTop;
        lastScrollTop5 = currentScrollTop;
    });
    scrollDownBtn5.addEventListener("click", () => {
        const isAtBottom = scrollArea5.scrollTop + scrollArea5.clientHeight >= scrollArea5.scrollHeight - 25;
        if (isAtBottom) {
            getPage5().then(() => {
                scrollArea5.scrollTop += scrollStep5;
                updateButtonColors5();
            });
        }else{
            scrollArea5.scrollTop += scrollStep5;
            updateButtonColors5();
        }

        const currentScrollTop = scrollArea5.scrollTop;
        lastScrollTop5 = currentScrollTop;
    });

    scrollArea5.addEventListener("scroll", () => {
        const currentScrollTop = scrollArea5.scrollTop;

        if (currentScrollTop > lastScrollTop5) {
            const isAtBottom = scrollArea5.scrollTop + scrollArea5.clientHeight >= scrollArea5.scrollHeight - 25;
            if (isAtBottom) getPage5();
        }
        updateButtonColors5();
        lastScrollTop5 = currentScrollTop;
    });
}

// 버튼 색상 변경
function updateButtonColors() {
    const scrollTop = scrollArea.scrollTop;
    const isAtBottom = scrollArea.scrollTop + scrollArea.clientHeight >= scrollArea.scrollHeight - 25;

    scrollUpBtn.disabled = scrollTop > EPSILON ? false : true;

    if(totalPages<page && isAtBottom) scrollDownBtn.disabled = true;
    else scrollDownBtn.disabled = false;
}
function updateButtonColors2() {
    const scrollTop = scrollArea2.scrollTop;
    const isAtBottom = scrollArea2.scrollTop + scrollArea2.clientHeight >= scrollArea2.scrollHeight - 25;

    scrollUpBtn2.disabled = scrollTop > EPSILON ? false : true;

    if(totalPages2<page2 && isAtBottom) scrollDownBtn2.disabled = true;
    else scrollDownBtn2.disabled = false;
}
function updateButtonColors3() {
    const scrollTop = scrollArea3.scrollTop;
    const isAtBottom = scrollArea3.scrollTop + scrollArea3.clientHeight >= scrollArea3.scrollHeight - 25;

    scrollUpBtn3.disabled = scrollTop > EPSILON ? false : true;

    if(totalPages3<page3 && isAtBottom) scrollDownBtn3.disabled = true;
    else scrollDownBtn3.disabled = false;
}
function updateButtonColors4() {
    const scrollTop = scrollArea4.scrollTop;
    const isAtBottom = scrollArea4.scrollTop + scrollArea4.clientHeight >= scrollArea4.scrollHeight - 25;

    scrollUpBtn4.disabled = scrollTop > EPSILON ? false : true;

    if(totalPages4<page4 && isAtBottom) scrollDownBtn4.disabled = true;
    else scrollDownBtn4.disabled = false;
}
function updateButtonColors5() {
    const scrollTop = scrollArea5.scrollTop;
    const isAtBottom = scrollArea5.scrollTop + scrollArea5.clientHeight >= scrollArea5.scrollHeight - 25;

    scrollUpBtn5.disabled = scrollTop > EPSILON ? false : true;

    if(totalPages5<page5 && isAtBottom) scrollDownBtn5.disabled = true;
    else scrollDownBtn5.disabled = false;
}
export function getDiscountBtnPage(){
    if(searchCategory===null && searchSelect.options.length > 0) {
        const temp = searchSelect.options[0].value;
        searchCategory = Number(temp.slice(6));
    }

    return new Promise((resolve) => {
        $.ajax({
            url:'/api/discountPage',
            method:'GET',
            data: {page:page, size:size, searchText:searchText, searchCategory:searchCategory},
            success:function(data){
                totalPages=data.page.totalPages;
                if(totalPages<page) finPage=true;
                const contents = data.content;
                contents.forEach(content => {
                    const newTr = document.createElement('tr');
                    newTr.id = 'area1'+content.id;

                    let tempCost = content.cost;
                    let unit = '%';
                    if(content.typeValue==true) {
                        tempCost = (tempCost+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                        unit='원';
                    }
                    [content.number, content.name, content.category, tempCost+unit].forEach(text => {
                        const newTd = document.createElement('td');
                        newTd.textContent = text;
                        newTr.appendChild(newTd);
                    });

                    defaultInsertPoint.appendChild(newTr);
                });
                if(searchCategory!==null && page===2) resetSortTable();
                resolve();
            },
            error: function(xhr) { console.log('error'); }
        });
        page++;
    });
}
export function getDiscountBtn(){

}
export function resetDiscountBtn(){

}
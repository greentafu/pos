let optionMap = new Map();
let discountMap = new Map();

let modalType;

// open Modal
function openOptionModal(e, type){
    if(type === 'option'){
        modalType = 'menu';
        selectedProduct = Number(e.id.slice(3));
        selectedProductCount = 1;
        basicPrice = Number(e.querySelector('.btnPrice').textContent.replace(/[^\d]/g, ''));

        document.getElementById('optionName').textContent = e.querySelector('.btnName').textContent;
        document.getElementById('countSpan1').textContent = selectedProductCount+'개';
        document.getElementById('option_all_price').textContent = (basicPrice+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원'
        document.getElementById('optionModal').style.display='';
    }else if(type === 'selectedOption'){
        if(selectedRow === '') return null;
        else{
            $.ajax({
                url:'/api/getOrderItemOptionDetail',
                method:'GET',
                data: {id:selectedRow},
                success:function(data){
                    const optionList = data.optionList;
                    optionList?.forEach(option => {
                        const categoryId = option.optionCategoryDTO.id;
                        optionMap.set('option_'+categoryId+'_'+option.id+'_selected', {name:option.displayName, price:option.optionPrice+''});
                        document.getElementById('option_'+categoryId+'_'+option.id).classList.add('selected');
                        document.getElementById('option_'+categoryId+'_default').classList.remove('selected');
                    });

                    modalType = 'row';
                    selectedProduct = data.orderItemDTO.productDTO.id;
                    selectedProductCount = data.orderItemDTO.productCount;
                    basicPrice = data.orderItemDTO.productDTO.productPrice;
                    tempDiscountPrice = data.orderItemDTO.totalDiscount;

                    document.getElementById('optionName').textContent = data.orderItemDTO.productDTO.displayName;
                    document.getElementById('countSpan1').textContent = selectedProductCount+'개';
                    showNamePriceBox('option');
                    document.getElementById('optionModal').style.display='';
                },
                error: function(xhr) { console.log('error'); }
            });
        }
    }
}
function openDiscountModal(type){
    if(type === 'allDiscount'){
        $.ajax({
            url:'/api/getOrderDiscountDetail',
            method:'GET',
            data: {waiting:waiting},
            success:function(data){
                const discountList = data.discountList;
                discountList?.forEach(discount => {
                    const categoryId = discount.discountCategoryDTO.id;
                    discountMap.set('discount_'+categoryId+'_'+discount.id+'_selected', {name:discount.displayName, price:discount.discountPrice+''});
                    document.getElementById('discount_'+categoryId+'_'+discount.id).classList.add('selected');
                    document.getElementById('discount_'+categoryId+'_default').classList.remove('selected');
                });

                modalType = 'all';
                selectedProductCount = 1;
                basicPrice = data.ordersDTO.totalPaymentAmount;
                tempDiscountPrice = 0;

                document.getElementById('discountName').textContent = '전체';
                document.getElementById('countSpan2').textContent = '';
                showNamePriceBox('discount');
                document.getElementById('discountModal').style.display='';
            },
            error: function(xhr) { console.log('error'); }
        });
    }else if(type === 'selectedDiscount'){
        if(selectedRow === '') return null;
        else{
            $.ajax({
                url:'/api/getOrderItemDiscountDetail',
                method:'GET',
                data: {id:selectedRow},
                success:function(data){
                    const discountList = data.discountList;
                    discountList?.forEach(discount => {
                        const categoryId = discount.discountCategoryDTO.id;
                        discountMap.set('discount_'+categoryId+'_'+discount.id+'_selected', {name:discount.displayName, price:discount.discountPrice+''});
                        document.getElementById('discount_'+categoryId+'_'+discount.id).classList.add('selected');
                        document.getElementById('discount_'+categoryId+'_default').classList.remove('selected');
                    });

                    modalType = 'row';
                    selectedProduct = data.orderItemDTO.productDTO.id;
                    selectedProductCount = data.orderItemDTO.productCount;
                    basicPrice = data.orderItemDTO.productPerUnit;
                    tempDiscountPrice = 0;

                    document.getElementById('discountName').textContent = data.orderItemDTO.productDTO.displayName;
                    document.getElementById('countSpan2').textContent = selectedProductCount+'개';
                    showNamePriceBox('discount');
                    document.getElementById('discountModal').style.display='';
                },
                error: function(xhr) { console.log('error'); }
            });
        }
    }
}
function openSoldOutModal(){
    document.getElementById('soldOutModal').style.display='';
}

// close Modal
function closeOrderModal(){
    document.getElementById("modalItem").style.display = "block";
    const allModal = document.querySelectorAll('.orderModal');
    allModal?.forEach(modal => modal.style.display='none');

    optionMap = new Map();
    discountMap = new Map();
    modalType = null;
    document.getElementById('option_all_name').textContent = '';
    document.getElementById('discount_all_name').textContent = '';
    selectedProductCount = 0;
    tempDiscountPrice = 0;

    const allOptionBtn = document.querySelectorAll('#optionBody button');
    allOptionBtn.forEach(btn => {
        if(btn.id.includes('default')) btn.classList.add('selected');
        else btn.classList.remove('selected');
    });

    const allDiscountBtn = document.querySelectorAll('#discountBody button');
    allDiscountBtn.forEach(btn => {
        if(btn.id.includes('default')) btn.classList.add('selected');
        else btn.classList.remove('selected');
    });

    resetSoldOutPage();
}

// click ModalContent
function clickModalContent(e, type){
    const category = e.closest('.plusBox');
    const allBtn = category.querySelectorAll('button');

    const multi = category.dataset.multi;

    if(e.id.slice(-7)==='default'){
        allBtn?.forEach(btn => {
            if (btn.id.slice(-7) === 'default') btn.classList.add('selected');
            else {
                btn.classList.remove('selected');
                if(type==='option') optionMap.delete(btn.id+'_selected');
                if(type==='discount') discountMap.delete(btn.id+'_selected');
            }
        });
    }else{
        if(multi==='false'){
            allBtn?.forEach(btn => {
                if (btn.id === e.id) {
                    if(!btn.classList.contains('selected')){
                        btn.classList.add('selected');

                        const name = btn.querySelector('.name').textContent;
                        const price = btn.querySelector('.price').textContent;
                        if(type==='option') optionMap.set(btn.id+'_selected', {name:name, price:price});
                        if(type==='discount') discountMap.set(btn.id+'_selected', {name:name, price:price});
                    }else{
                        document.getElementById(category.id+'_default').classList.add('selected');
                        btn.classList.remove('selected');
                        if(type==='option') optionMap.delete(btn.id+'_selected');
                        if(type==='discount') discountMap.delete(btn.id+'_selected');
                    }
                }else {
                    btn.classList.remove('selected');
                    if(type==='option') optionMap.delete(btn.id+'_selected');
                    if(type==='discount') discountMap.delete(btn.id+'_selected');
                }
            });
        }else{
            if(!e.classList.contains('selected')){
                e.classList.add('selected');
                document.getElementById(category.id+'_default').classList.remove('selected');

                const name = e.querySelector('.name').textContent;
                const price = e.querySelector('.price').textContent;
                if(type==='option') optionMap.set(e.id+'_selected', {name:name, price:price});
                if(type==='discount') discountMap.set(e.id+'_selected', {name:name, price:price});
            }else{
                e.classList.remove('selected');
                if(type==='option') {
                    optionMap.delete(e.id+'_selected');
                    const defaultFlag = Array.from(optionMap.keys()).some(key => key.startsWith(category.id));
                    if(!defaultFlag) document.getElementById(category.id+'_default').classList.add('selected');
                }
                if(type==='discount') {
                    discountMap.delete(e.id+'_selected');
                    const defaultFlag = Array.from(discountMap.keys()).some(key => key.startsWith(category.id));
                    if(!defaultFlag) document.getElementById(category.id+'_default').classList.add('selected');
                }
            }
        }
    }
    showNamePriceBox(type);
}
function showNamePriceBox(type){
    if(type==='option'){
        const names = [...optionMap.values()].map(v => v.name);
        const nameResult = names.length ? names.join(' + ') : '';
        const priceResult = [...optionMap.values()].reduce((sum, item) => sum + Number(item.price.replace(/[^\d]/g, '')), basicPrice);

        document.getElementById('option_all_name').textContent = nameResult;
        document.getElementById('option_all_price').textContent = (priceResult * selectedProductCount - tempDiscountPrice + '').replace(/\B(?=(\d{3})+(?!\d))/g, ',') + '원';
    }else if(type==='discount'){
        const names = [...discountMap.values()].map(v => v.name);
        const nameResult = names.length ? names.join(' + ') : '';

        let total = basicPrice;
        let percent = 0;
        discountMap.forEach((value, key) => {
            if(value.price.includes('%')) {
                const price = value.price.replace(/[^\d.]/g, '');
                percent += Number(price);
            }else {
                const price = value.price.replace(/[^\d]/g, '');
                total -= Number(price);
            }
        });
        const priceResult = total - (total * (percent / 100));

        document.getElementById('discount_all_name').textContent = nameResult;
        document.getElementById('discount_all_price').textContent = (priceResult*selectedProductCount + '').replace(/\B(?=(\d{3})+(?!\d))/g, ',') + '원';
    }
}

// save option/discount
function saveOption(){
    const keys = [...optionMap.keys()].map(key => key.split('_')[2]);
    if(modalType==='row'){
        $.ajax({
            url:'/api/updateOrderItemOption',
            method:'PUT',
            contentType: "application/json",
            data: JSON.stringify({orderItemId:selectedRow, optionList:keys}),
            success:function(data){ getPage(); },
            error: function(xhr) { console.log('error'); }
        });
    }else {
        $.ajax({
            url:'/api/saveOrderItemOption',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({waiting:waiting, product:selectedProduct, optionList:keys}),
            success:function(data){ getPage(); },
            error: function(xhr) { console.log('error'); }
        });
    }
}
function saveDiscount(){
    const keys = [...discountMap.keys()].map(key => key.split('_')[2]);
    if(modalType==='row'){
        $.ajax({
            url:'/api/updateOrderItemDiscount',
            method:'PUT',
            contentType: "application/json",
            data: JSON.stringify({orderItemId:selectedRow, discountList:keys}),
            success:function(data){ getPage(); },
            error: function(xhr) { console.log('error'); }
        });
    }else{
        $.ajax({
            url:'/api/updateOrderDiscount',
            method:'PUT',
            contentType: "application/json",
            data: JSON.stringify({waiting:waiting, discountList:keys}),
            success:function(data){ getPage(); },
            error: function(xhr) { console.log('error'); }
        });
    }
}
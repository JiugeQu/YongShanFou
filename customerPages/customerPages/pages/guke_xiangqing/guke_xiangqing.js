axios.defaults.baseURL = 'http://127.0.0.1:4523/m1/3592222-0-default';
document.addEventListener('DOMContentLoaded', function () {
    // 提取URL参数中的订单ID
    const urlParams = new URLSearchParams(window.location.search);
    const orderId = urlParams.get('orderId');

    // 如果存在订单ID，则获取并显示订单详情
    // if (orderId) {
        fetchOrderDetails(orderId);
    // }
});

function fetchOrderDetails() {
    // 使用订单ID向后端请求订单详细信息的逻辑
    // var myHeaders = new Headers();
    // myHeaders.append("token", "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjozLCJwaG9uZSI6IjE5ODI5NjY5MzUyIiwibmFtZSI6InN0YWZm5bCP56WlIiwiaWQiOjUsImV4cCI6MTY5OTQzNzI4NX0.l-ZllhQQjRGOedtW4xEd4jGSCMfD0phEz92yUoFwODk"); // 替换为实际的token

    // var requestOptions = {
    //     method: 'GET',
    //     headers: myHeaders,
    //     redirect: 'follow'
    // };

    // // 使用fetch请求订单详情接口
    // fetch("http://127.0.0.1:4523/m1/3573397-0-default/staff/order/" + orderId, requestOptions)
    //     .then(response => response.json())  // 假设响应是JSON格式
    //     .then(data => {
    //         displayData(data);
    //         attachConfirmationListener(data);
    //     })
    //     .catch(error => console.error('error', error));
    var axiosConfig = {
        headers: {
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjozLCJwaG9uZSI6IjE5ODI5NjY5MzUyIiwibmFtZSI6InN0YWZm5bCP56WlIiwiaWQiOjUsImV4cCI6MTY5OTQzNzI4NX0.l-ZllhQQjRGOedtW4xEd4jGSCMfD0phEz92yUoFwODk", // 替换为实际的token
        }
    };
    // 使用 Axios 请求订单详情接口
    // axios.get(`/staff/order/${orderId}`, axiosConfig)
    axios.get('/customer/order/1721848915910721536', axiosConfig)
        .then(response => response.data)
        .then(data => {
            displayData(data);
            attachConfirmationListener(data);
        })
        .catch(error => console.error('error', error));
}
      
function displayData(data) {
    var ordersContainer = document.getElementById('orderDetailsContainer');
    ordersContainer.innerHTML = '';

    var order=data.data;

    var orderElement = document.createElement('div');
    orderElement.classList.add('order-container','flex-col', 'list-item' );
        
    // 添加订单日期和状态
    var orderInfoElement = document.createElement('div');
    orderInfoElement.classList.add('flex-row', 'justify-between', 'items-center', 'group_2');
        
    var orderDateElement = document.createElement('div');
    orderDateElement.classList.add('flex-row', 'items-center', 'space-x-10');
        
    var orderImageElement = document.createElement('img');
    orderImageElement.classList.add('shrink-0', 'image');
    orderImageElement.src = "./picture/clock.png"; 
        
    var orderDateTextElement = document.createElement('span');
    orderDateTextElement.classList.add('font_4');
    orderDateTextElement.textContent = order.createTime; // 替换为实际包含日期的属性
        
    orderDateElement.appendChild(orderImageElement);
    orderDateElement.appendChild(orderDateTextElement);
        
    var orderStatusElement = document.createElement('span');
    orderStatusElement.classList.add('font_3', 'text_3');
    orderStatusElement.textContent = order.state === 1 ? '待接单' : '';
    orderStatusElement.textContent = order.state === 2 ? '备餐中' : '';
    orderStatusElement.textContent = order.state === 3 ? '待配送' : '';
    orderStatusElement.textContent = order.state === 4 ? '配送中' : '';
    orderStatusElement.textContent = order.state === 5 ? '待评价' : '';
    orderStatusElement.textContent = order.state === 6 ? '已评价' : '';
        
    orderInfoElement.appendChild(orderDateElement);
    orderInfoElement.appendChild(orderStatusElement);
        
    orderElement.appendChild(orderInfoElement);
        
    // 添加订单项
    var orderItemsElement = document.createElement('div');
    orderItemsElement.classList.add('flex-col', 'list_2');
        
    order.orderDishes.forEach(dish => {
        // 添加商品图像、名称和价格
        var orderItemElement = document.createElement('div');
        orderItemElement.classList.add('flex-row', 'justify-between', 'items-center', 'list-item_2', 'group_3');

        // 添加商品图像
        var itemImageElement = document.createElement('img');
        itemImageElement.classList.add('shrink-0', 'image_2');
        itemImageElement.src = dish.dishImg; 

        // 添加商品名称和价格容器
        var itemNamePriceContainer = document.createElement('div');
        itemNamePriceContainer.classList.add('flex-row', 'justify-between', 'items-center', 'space-x-2'); // 样式调整

        // 添加商品名称
        var itemNameElement = document.createElement('span');
        itemNameElement.classList.add('font_1', 'text_4', 'text-left'); // 左对齐的样式
        itemNameElement.textContent = dish.dishName; // 替换为实际包含商品名称的属性

        // 添加商品价格
        var itemPriceElement = document.createElement('span');
        itemPriceElement.classList.add('font_5', 'text_5', 'text-right'); // 右对齐的样式
        itemPriceElement.textContent = `￥${dish.dishPrice}`; // 替换为实际包含商品价格的属性

        // 将商品名称和价格添加到容器
        itemNamePriceContainer.appendChild(itemNameElement);
        itemNamePriceContainer.appendChild(itemPriceElement);

        // 将商品图像和名称/价格容器添加到订单项
        orderItemElement.appendChild(itemImageElement);
        orderItemElement.appendChild(itemNamePriceContainer);

        // 将商品图像和名称/价格容器添加到订单项
        orderItemElement.appendChild(itemImageElement);
        orderItemElement.appendChild(itemNamePriceContainer);

        // 将商品图像和名称/价格容器添加到订单项
        orderItemElement.appendChild(itemImageElement);
        orderItemElement.appendChild(itemNamePriceContainer);

        // 将订单项添加到订单项容器
        orderItemsElement.appendChild(orderItemElement);
    });
        
    orderElement.appendChild(orderItemsElement);
        
    // 添加订单总价和客户信息
    var orderTotalElement = document.createElement('div');
    orderTotalElement.classList.add('flex-row', 'justify-between', 'items-center', 'group_6');
        
    var orderTotalInfoElement = document.createElement('div');
    orderTotalInfoElement.classList.add('flex-row', 'items-center', 'space-x-30');
        
    var totalPriceElement = document.createElement('div');
    totalPriceElement.classList.add('flex-row', 'items-center', 'self-stretch', 'space-x-12');
        
    var totalPriceTextElement = document.createElement('span');
    totalPriceTextElement.classList.add('font_7');
    totalPriceTextElement.textContent = '总价';
        
    var totalPriceValueElement = document.createElement('span');
    totalPriceValueElement.classList.add('font_8', 'text_8');
    totalPriceValueElement.textContent = order.totalPrice; // 替换为实际包含总价的属性
        
    var currencyElement = document.createElement('span');
    currencyElement.classList.add('font_7', 'text_7');
    currencyElement.textContent = '元';
        
    totalPriceElement.appendChild(totalPriceTextElement);
    totalPriceElement.appendChild(totalPriceValueElement);
    totalPriceElement.appendChild(currencyElement);
        
    orderTotalInfoElement.appendChild(totalPriceElement);
    orderTotalElement.appendChild(orderTotalInfoElement);
                
    //备注内容
    var orderNoteElement = document.createElement('div');
    orderNoteElement.classList.add('flex-row', 'justify-between',  'group_12');

    var orderNtextElement = document.createElement('div');
    orderNtextElement.classList.add('font_9');
    orderNtextElement.textContent = '备注';

    var orderNotesElement = document.createElement('span');
    orderNotesElement.classList.add('font_2y','text-wrapper_y','text_14y');
    orderNotesElement.textContent=order.note;

    orderNoteElement.appendChild(orderNtextElement);
    orderNoteElement.appendChild(orderNotesElement);
    orderElement.appendChild(orderNoteElement);
        
                
    //配送详情 mokuai2
    var orderElement2 = document.createElement('div');
    orderElement2.classList.add('order-container','flex-col', 'list-item' );
                
    //配送情况
    var orderInfo2Element = document.createElement('div');
    orderInfo2Element.classList.add('flex-row', 'justify-between', 'items-center', 'group_2');

    var orderTextElement = document.createElement('span');
    orderTextElement.classList.add('font_9');
    orderTextElement.textContent = '配送情况';

    var orderStatus2Element = document.createElement('span');
    orderStatus2Element.classList.add('font_3','text_3');
    orderStatus2Element.textContent = order.state === 3?'待配送':'';
    orderStatus2Element.textContent = order.state === 4?'配送中':'';
    orderStatus2Element.textContent = order.state === 5?'已送达':'';
    orderStatus2Element.textContent = order.state === 5?'已送达':'';

    orderInfo2Element.appendChild(orderTextElement);
    orderInfo2Element.appendChild(orderStatus2Element);
    orderElement2.appendChild(orderInfo2Element);

    //地址
    var orderPlaceElement = document.createElement('div');
    orderPlaceElement.classList.add('flex-row', 'justify-between',  'group_12');

    var orderPtextElement = document.createElement('div');
    orderPtextElement.classList.add('font_9');
    orderPtextElement.textContent = '地址';

    var orderAddressElement = document.createElement('span');
    orderAddressElement.classList.add('font_2y','text-wrapper_y','text_14y');
    orderAddressElement.textContent=order.address;

    orderPlaceElement.appendChild(orderPtextElement);
    orderPlaceElement.appendChild(orderAddressElement);
    orderElement2.appendChild(orderPlaceElement);
                
    //订单人
    var orderPeopleElement = document.createElement('div');
    orderPeopleElement.classList.add('flex-row', 'justify-between', 'items-center', 'group_12');

    var orderPeopletextElement = document.createElement('span');
    orderPeopletextElement.classList.add('font_9');
    orderPeopletextElement.textContent = '订单人';

    var orderPeoplenameElement = document.createElement('span');
    orderPeoplenameElement.classList.add('font_9');
    orderPeoplenameElement.textContent = order.customerName;  
                
    orderPeopleElement.appendChild(orderPeopletextElement);
    orderPeopleElement.appendChild(orderPeoplenameElement);
    orderElement2.appendChild(orderPeopleElement);    

    //手机号
    var orderPhoneElement = document.createElement('div');
    orderPhoneElement.classList.add('flex-row', 'justify-between', 'items-center', 'group_12');

    var orderPhonetextElement = document.createElement('span');
    orderPhonetextElement.classList.add('font_9');
    orderPhonetextElement.textContent = '手机号';

    var ordernumberElement = document.createElement('span');
    ordernumberElement.classList.add('font_9');
    ordernumberElement.textContent = order.customerPhone;  
                
    orderPhoneElement.appendChild(orderPhonetextElement);
    orderPhoneElement.appendChild(ordernumberElement);
    orderElement2.appendChild(orderPhoneElement);

    //期望送达时间
    var orderTimeElement = document.createElement('div');
    orderTimeElement.classList.add('flex-row', 'justify-between', 'items-center', 'group_12');

    var orderTimetextElement = document.createElement('span');
    orderTimetextElement.classList.add('font_9');
    orderTimetextElement.textContent = '期望送达时间';

    var ordertimeElement = document.createElement('span');
    ordertimeElement.classList.add('font_9');
    ordertimeElement.textContent = order.expectArriveTime;  
                
    orderTimeElement.appendChild(orderTimetextElement);
    orderTimeElement.appendChild(ordertimeElement);
    orderElement2.appendChild(orderTimeElement);

    //状态456 配送员姓名电话
    if(order.state==4||order.state==5||order.state==6)
    {
        var orderDElement = document.createElement('div');
        orderDElement.classList.add('flex-row', 'justify-between', 'items-center', 'group_12');

        var orderDtextElement = document.createElement('span');
        orderDtextElement.classList.add('font_9');
        orderDtextElement.textContent = '实际送达时间';

        var orderDnameElement = document.createElement('span');
        orderDnameElement.classList.add('font_9');
        orderDnameElement.textContent = order.realArriveTime;  

        var orderDnumberElement = document.createElement('span');
        orderDnumberElement.classList.add('font_9');
        orderDnumberElement.textContent = order.deliverPhone; 
                    
        orderDElement.appendChild(orderDtextElement);
        orderDElement.appendChild(orderDnameElement);
        orderDElement.appendChild(orderDnumberElement);
        orderElement2.appendChild(orderDElement);
    }

    //状态6 评价 
    var orderElement3 = document.createElement('div');  
    orderElement3.classList.add('order-container','flex-col', 'list-item' );
        
    //评价
    var orderPingjiaElement = document.createElement('div');
    orderPingjiaElement.classList.add('flex-row', 'justify-between',  'group_13');

    var orderPjtextElement = document.createElement('div');
    orderPjtextElement.classList.add('font_9');
    orderPjtextElement.textContent = '评价';

    var orderPingElement = document.createElement('span');
    orderPingElement.classList.add('font_2y','text-wrapper_y','text_14y');
    orderPingElement.textContent=order.note;

    orderPingjiaElement.appendChild(orderPjtextElement);
    orderPingjiaElement.appendChild(orderPingElement);
    orderElement3.appendChild(orderPingjiaElement);
        
    if(order.state==6)
    ordersContainer.appendChild(orderElement3);
    ordersContainer.appendChild(orderElement);
    ordersContainer.appendChild(orderElement2);
          
}

function attachConfirmationListener(data){
    var order=data.data;
    var confirmButton = document.querySelector('.text-wrapper_5z');
    if(confirmButton){
        confirmButton.addEventListener("click",function(){
            updateOrderStatus('confirm');
            sendPutRequest(order.code, order.state);
            updateOrderStatus2();
        });
    }

    // 找到备餐中、已出餐和取消订单的元素并添加点击事件监听器
    var prepareOrderButton = document.querySelector('.text-wrapper_2z1');
    var serveOrderButton = document.querySelector('.text-wrapper_2z2');
    var cancelOrderButton = document.querySelector('.text-wrapper_2z3');

    // 为备餐中按钮添加点击事件监听器
    if (prepareOrderButton) {
        prepareOrderButton.addEventListener("click", function() {
            updateOrderStatus('prepare');
            order.state=2;
        });
    }   
    // 为已出餐按钮添加点击事件监听器
    if (serveOrderButton) {
        serveOrderButton.addEventListener("click", function() {
            updateOrderStatus('serve');
            order.state=3;
        });
    }
    // 为取消订单按钮添加点击事件监听器
    if (cancelOrderButton) {
        cancelOrderButton.addEventListener("click", function() {
            updateOrderStatus('cancel');
            order.state=7;
        });
    } 

}

// 更新订单状态的函数
function updateOrderStatus(newStatus) {
    // 获取备餐中、已出餐和取消订单的元素
    var prepareOrderButton = document.querySelector('.text-wrapper_2z1');
    var serveOrderButton = document.querySelector('.text-wrapper_2z2');
    var cancelOrderButton = document.querySelector('.text-wrapper_2z3');
    var confirmButton = document.querySelector('.text-wrapper_5z');

    // 根据新状态更改样式
    switch (newStatus) {
        case 'prepare':
            // 备餐中
            prepareOrderButton.closest('.text-wrapper_2z1').classList.add('text-wrapper_3z');
            serveOrderButton.closest('.text-wrapper_2z2').classList.remove('text-wrapper_3z');
            cancelOrderButton.closest('.text-wrapper_2z3').classList.remove('text-wrapper_3z');
            confirmButton.closest('.text-wrapper_5z').classList.remove('text-wrapper_5z5'); 
            break;
        case 'serve':
            // 已出餐
            serveOrderButton.closest('.text-wrapper_2z2').classList.add('text-wrapper_3z');
            prepareOrderButton.closest('.text-wrapper_2z1').classList.remove('text-wrapper_3z');
            cancelOrderButton.closest('.text-wrapper_2z3').classList.remove('text-wrapper_3z');
            confirmButton.closest('.text-wrapper_5z').classList.remove('text-wrapper_5z5'); 
            break;
        case 'cancel':
            // 取消订单
            cancelOrderButton.closest('.text-wrapper_2z3').classList.add('text-wrapper_3z');
            prepareOrderButton.closest('.text-wrapper_2z1').classList.remove('text-wrapper_3z');
            serveOrderButton.closest('.text-wrapper_2z2').classList.remove('text-wrapper_3z');
            confirmButton.closest('.text-wrapper_5z').classList.remove('text-wrapper_5z5'); 
            break;
        case 'confirm':
            // 确认订单
            confirmButton.closest('.text-wrapper_5z').classList.add('text-wrapper_5z5');
            break;
        default:
            console.error("Invalid order state");
    }
}

function updateOrderStatus2() {
    // 获取备餐中、已出餐和取消订单的元素
    var prepareOrderButton = document.querySelector('.text-wrapper_2z1');
    var serveOrderButton = document.querySelector('.text-wrapper_2z2');
    var cancelOrderButton = document.querySelector('.text-wrapper_2z3');
    var confirmButton = document.querySelector('.text-wrapper_5z');
    
    prepareOrderButton.closest('.text-wrapper_2z1').classList.remove('text-wrapper_3z');
    serveOrderButton.closest('.text-wrapper_2z2').classList.remove('text-wrapper_3z');
    cancelOrderButton.closest('.text-wrapper_2z3').classList.remove('text-wrapper_3z');          
}
  












      
  
    




      

document.addEventListener('DOMContentLoaded', function () {
    // 提取URL参数中的订单ID
    const urlParams = new URLSearchParams(window.location.search);
    const orderId = urlParams.get('orderId');

    // 如果存在订单ID，则获取并显示订单详情
    if (orderId) {
        fetchOrderDetails(orderId);
    }
});

function fetchOrderDetails(orderId) {
    // 使用订单ID向后端请求订单详细信息的逻辑
    // var myHeaders = new Headers();
    // myHeaders.append("token", "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjozLCJwaG9uZSI6IjE5ODI5NjY5MzUyIiwibmFtZSI6InN0YWZm5bCP56WlIiwiaWQiOjUsImV4cCI6MTY5OTQzNzI4NX0.l-ZllhQQjRGOedtW4xEd4jGSCMfD0phEz92yUoFwODk"); // 替换为实际的token

    // var requestOptions = {
    //     method: 'GET',
    //     headers: myHeaders,
    //     redirect: 'follow'
    // };

    // // 使用fetch请求订单详情接口
    // // fetch("http://127.0.0.1:4523/m1/3573397-0-default/staff/order/" + orderId, requestOptions)
    // fetch("http://127.0.0.1:4523/m1/3576146-0-default/staff/order/state/1", requestOptions)
    //     .then(response => response.json())  // 假设响应是JSON格式
    //     .then(data => displayData(data))
    //     .catch(error => console.error('error', error));
    var axiosConfig = {
        headers: {
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjozLCJwaG9uZSI6IjE5ODI5NjY5MzUyIiwibmFtZSI6InN0YWZm5bCP56WlIiwiaWQiOjUsImV4cCI6MTY5OTQzNzI4NX0.l-ZllhQQjRGOedtW4xEd4jGSCMfD0phEz92yUoFwODk", // 替换为实际的token
            "User-Agent": "Apifox/1.0.0 (https://apifox.com)"
        }
    };

    // 使用 Axios 请求订单详情接口
    axios.get(`http://127.0.0.1:4523/m1/3573397-0-default/staff/order/${orderId}`, axiosConfig)
        .then(response => response.data)
        .then(data => {
            displayData(data);
        })
        .catch(error => console.error('error', error));
}

// var myHeaders = new Headers();
// myHeaders.append("token", "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjozLCJwaG9uZSI6IjE5ODI5NjY5MzUyIiwibmFtZSI6InN0YWZm5bCP56WlIiwiaWQiOjUsImV4cCI6MTY5OTQzNzI4NX0.l-ZllhQQjRGOedtW4xEd4jGSCMfD0phEz92yUoFwODk");
// myHeaders.append("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
      
// var requestOptions = {
//     method: 'GET',
//     headers: myHeaders,
//     redirect: 'follow'
//  };
      
// fetch("http://127.0.0.1:4523/m1/3576146-0-default/staff/order/1721848915910721536", requestOptions)
//     .then(response => response.json())  // 解析JSON格式的响应
//     .then(data => displayData(data))
//     .catch(error => console.log('error', error));
      
    function displayData(data) {
        var ordersContainer = document.getElementById('contentDetail');
        ordersContainer.innerHTML = '';

        var order=data.data;

        //模块1
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
        orderDateTextElement.textContent = order.expectArriveTime; 
        
        orderDateElement.appendChild(orderImageElement);
        orderDateElement.appendChild(orderDateTextElement);    
        orderInfoElement.appendChild(orderDateElement);
 
        orderElement.appendChild(orderInfoElement);
              
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
        orderElement.appendChild(orderPlaceElement);
                
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
        orderElement.appendChild(orderPeopleElement);

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
        orderElement.appendChild(orderPhoneElement);


        //配送员姓名电话
        if(order.state==4||order.state==5||order.state==6)
        {
            var orderDElement = document.createElement('div');
            orderDElement.classList.add('flex-row', 'justify-between', 'items-center', 'group_12');

            var orderDtextElement = document.createElement('span');
            orderDtextElement.classList.add('font_9');
            orderDtextElement.textContent = '送餐员';

            var orderDnameElement = document.createElement('span');
            orderDnameElement.classList.add('font_9');
            orderDnameElement.textContent = order.deliverName;  

            var orderDnumberElement = document.createElement('span');
            orderDnumberElement.classList.add('font_9');
            orderDnumberElement.textContent = order.deliverPhone; 
                    
            orderDElement.appendChild(orderDtextElement);
            orderDElement.appendChild(orderDnameElement);
            orderDElement.appendChild(orderDnumberElement);
            orderElement.appendChild(orderDElement);
        }
                
     
        //第二块
        var orderElement2 = document.createElement('div');  
        orderElement2.classList.add('order-container','flex-col', 'list-item' );
        
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
        orderElement2.appendChild(orderPingjiaElement);


        //第三块
        var orderElement3 = document.createElement('div');
        orderElement3.classList.add('order-container','flex-col', 'list-item' );

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
        
        orderElement3.appendChild(orderItemsElement);
        
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

        orderElement3.appendChild(orderTotalElement);
        
        ordersContainer.appendChild(orderElement);
        ordersContainer.appendChild(orderElement2);
        ordersContainer.appendChild(orderElement3);
        
          
    }
      

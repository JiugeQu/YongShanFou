// var myHeaders = new Headers();
// myHeaders.append("token", "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTkzNjI1Mjh9.rrnU4zJeWfBx5BkBnCygXzuIu0zjMcakp0439frQbLM");
// myHeaders.append("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
      
// var requestOptions = {
//     method: 'GET',
//     headers: myHeaders,
//     redirect: 'follow'
//  };
      
// fetch("http://127.0.0.1:4523/m1/3576146-0-default/staff/order/state/1", requestOptions)
//     .then(response => response.json())  // 解析JSON格式的响应
//     .then(data => displayData(data))
//     .catch(error => console.log('error', error));

//    // 建立SSE连接
// var eventSource = new EventSource("http://127.0.0.1:4523/sse-endpoint");

// eventSource.onmessage = function (event) {
//     // 检查消息是否为"data:refresh"
//     if (event.data === "refresh") {
//         // 如果是刷新信号，重新获取数据并渲染
//         fetchDataAndRender();
//     } else {
//         // 如果不是刷新信号，解析JSON数据并显示
//         var data = JSON.parse(event.data);
//         displayData(data);
//     }
// };

// eventSource.onerror = function (error) {
//     console.error('SSE错误:', error);
//     eventSource.close();
// };


axios.get("http://127.0.0.1:4523/m1/3576146-0-default/staff/order/state/1", {
    headers: {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTkzNjI1Mjh9.rrnU4zJeWfBx5BkBnCygXzuIu0zjMcakp0439frQbLM", 
        // "User-Agent": "Apifox/1.0.0 (https://apifox.com)"
    }
})
.then(response => response.data)
.then(data => displayData(data))
.catch(error => console.log('error', error));

// 建立SSE连接
var eventSource = new EventSource("http://127.0.0.1:4523/sse-endpoint");

eventSource.onmessage = function (event) {
    if (event.data === "refresh") {
        fetchDataAndRender();
    } else {
        axios.get("http://127.0.0.1:4523/m1/3576146-0-default/staff/order/state/1", {
            headers: {
                "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTkzNjI1Mjh9.rrnU4zJeWfBx5BkBnCygXzuIu0zjMcakp0439frQbLM" 
            }
        })
        .then(response => response.data)
        .then(data => displayData(data))
        .catch(error => console.log('error', error));
    }
};

eventSource.onerror = function (error) {
    console.error('SSE错误:', error);
    eventSource.close();
};

function fetchDataAndRender() {
    axios.get("http://127.0.0.1:4523/m1/3576146-0-default/staff/order/state/1", {
        headers: {
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTkzNjI1Mjh9.rrnU4zJeWfBx5BkBnCygXzuIu0zjMcakp0439frQbLM" 
        }
    })
    .then(response => response.data)
    .then(data => displayData(data))
    .catch(error => console.log('error', error));
}



// // 获取数据并渲染函数
// function fetchDataAndRender() {
//     var myHeaders = new Headers();
//     myHeaders.append("token", "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTkzNjI1Mjh9.rrnU4zJeWfBx5BkBnCygXzuIu0zjMcakp0439frQbLM"); // 请替换成你的访问令牌

//     var requestOptions = {
//         method: 'GET',
//         headers: myHeaders,
//         redirect: 'follow'
//     };

//     fetch("http://127.0.0.1:4523/m1/3576146-0-default/staff/order/state/1", requestOptions)
//         .then(response => response.json())
//         .then(data => displayData(data))
//         .catch(error => console.log('error', error));
// }
      
    function displayData(data) {
        var ordersContainer = document.getElementById('output');
        ordersContainer.innerHTML = '';

        // if(data.state === 1){
            data.data.forEach(order => {
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
        
                orderInfoElement.appendChild(orderDateElement);
                orderInfoElement.appendChild(orderStatusElement)
        
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
                    itemImageElement.src = dish.dishImg; // 替换为实际包含商品图像URL的属性

                   // 添加商品名称和价格容器
                    var itemNamePriceContainer = document.createElement('div');
                    itemNamePriceContainer.classList.add('flex-row', 'justify-between', 'items-center', 'space-x-2'); 

                    // 添加商品名称
                    var itemNameElement = document.createElement('span');
                    itemNameElement.classList.add('font_1', 'text_4', 'text-left'); 
                    itemNameElement.textContent = dish.dishName; // 替换为实际包含商品名称的属性

                    // 添加商品价格
                    var itemPriceElement = document.createElement('span');
                    itemPriceElement.classList.add('font_5', 'text_5', 'text-right'); 
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
        
                var customerInfoElement = document.createElement('div');
                customerInfoElement.classList.add('flex-row', 'space-x-12');
        
                var customerNameTextElement = document.createElement('span');
                customerNameTextElement.classList.add('font_7');
                customerNameTextElement.textContent = '订单人';
        
                var customerNameElement = document.createElement('span');
                customerNameElement.classList.add('font_3');
                customerNameElement.textContent = order.customerName; // 替换为实际包含客户名称的属性
        
                customerInfoElement.appendChild(customerNameTextElement);
                customerInfoElement.appendChild(customerNameElement);
        
                orderTotalInfoElement.appendChild(totalPriceElement);
                orderTotalInfoElement.appendChild(customerInfoElement);
        
                orderTotalElement.appendChild(orderTotalInfoElement);
        
                // 添加 "查看详情" 元素
                var viewDetailsElement = document.createElement('div');
                viewDetailsElement.classList.add('flex-col', 'justify-start', 'items-center', 'text-wrapper_4');
        
                var viewDetailsTextElement = document.createElement('span');
                viewDetailsTextElement.classList.add('font_7');
                viewDetailsTextElement.textContent = '查看详情';

                // 添加点击事件
                viewDetailsElement.addEventListener('click', function () {
                    // 导航到订单详情页面，并传递订单ID
                    // window.location.href = './订单详情.html?orderId=' + '1721848915910721536'order.code;
                    window.location.href = './order _ details.html?orderId=' + order.code; // 根据实际数据结构调整
                });

                viewDetailsElement.appendChild(viewDetailsTextElement);
                orderTotalElement.appendChild(viewDetailsElement);

                orderElement.appendChild(orderTotalElement);
                ordersContainer.appendChild(orderElement);
                
            });

        // }
    }
      

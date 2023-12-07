axios.defaults.baseURL = 'http://127.0.0.1:4523/m1/3592222-0-default';

// 获取订单
// token暂无
axios.get('/customer/order/before/arrive', {
    headers: {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTkzNjI1Mjh9.rrnU4zJeWfBx5BkBnCygXzuIu0zjMcakp0439frQbLM"
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
        axios.get("/customer/order/before/arrive", {
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
    axios.get("/customer/order/before/arrive", {
        headers: {
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTkzNjI1Mjh9.rrnU4zJeWfBx5BkBnCygXzuIu0zjMcakp0439frQbLM" 
        }
    })
    .then(response => response.data)
    .then(data => displayData(data))
    .catch(error => console.log('error', error));
}
      
    function displayData(data) {
        var ordersContainer = document.getElementById('output');
        ordersContainer.innerHTML = '';

            data.data.forEach(order => {
                var orderElement = document.createElement('div');
                orderElement.classList.add('order-container','flex-col', 'list-item', );
        
                // 添加订单状态
                var orderInfoElement = document.createElement('div');
                orderInfoElement.classList.add('flex-row', 'justify-between', 'items-center', 'group_22');
        
                var orderDateElement = document.createElement('div');
                orderDateElement.classList.add('flex-row', 'items-center', 'space-x-10');
        
                var orderStatusElement = document.createElement('span');
                orderStatusElement.classList.add('font_3', 'text_3');
                if (order.state === 1) {
                    orderStatusElement.textContent = '待接单';
                } else if (order.state === 2) {
                    orderStatusElement.textContent = '备餐中';
                } else if (order.state === 3) {
                    orderStatusElement.textContent = '待配送';
                } else if (order.state === 4) {
                    orderStatusElement.textContent = '配送中';
                } else {
                    orderStatusElement.textContent = '已送达'; // 一般不会进入这里，apifox随机生成大于4的模拟数据
                }

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
                    itemImageElement.src = dish.dishImg; // 替换为实际包含商品图像URL的属性

                    // 添加商品名称和价格容器
                    var itemNamePriceContainer = document.createElement('div');
                    itemNamePriceContainer.classList.add('flex-row', 'justify-between', 'items-center', 'space-x-2'); // 样式调整

                    // 添加商品名称
                    var itemNameElement = document.createElement('span');
                    itemNameElement.classList.add('font_1', 'text_4', 'text-left'); // 左对齐的样式
                    itemNameElement.textContent = dish.dishName; // 替换为实际包含商品名称的属性

                    // 将商品名称和价格添加到容器
                    itemNamePriceContainer.appendChild(itemNameElement);

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
        
                var deliverInfoElement = document.createElement('div');
                deliverInfoElement.classList.add('flex-row', 'space-x-12');
                
                 if (order.state === 4) { // 显示送餐员姓名
                    var deliverNameTextElement = document.createElement('span');
                    deliverNameTextElement.classList.add('font_7');
                    deliverNameTextElement.textContent = '送餐员';
            
                    var deliverNameElement = document.createElement('span');
                    deliverNameElement.classList.add('font_3');
                    deliverNameElement.textContent = order.deliverName; 
            
                    deliverInfoElement.appendChild(deliverNameTextElement);
                    deliverInfoElement.appendChild(deliverNameElement);
                }
        
                orderTotalInfoElement.appendChild(totalPriceElement);
                orderTotalInfoElement.appendChild(deliverInfoElement);
        
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
                    // window.location.href = './订单详情.html?orderId=' + '1721848915910721536';
                    window.location.href = '../guke_xiangqing/guke_xiangqing.html?orderId=' + order.code; // 根据实际数据结构调整
                });
        
                viewDetailsElement.appendChild(viewDetailsTextElement);
                orderTotalElement.appendChild(viewDetailsElement);
        
                orderElement.appendChild(orderTotalElement);
        
                ordersContainer.appendChild(orderElement);
            });
    }
      

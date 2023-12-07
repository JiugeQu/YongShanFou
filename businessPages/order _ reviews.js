// var myHeaders = new Headers();
// myHeaders.append("token", "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTkzNjI1Mjh9.rrnU4zJeWfBx5BkBnCygXzuIu0zjMcakp0439frQbLM");
// myHeaders.append("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
      
// var requestOptions = {
//     method: 'GET',
//     headers: myHeaders,
//     redirect: 'follow'
//  };
      
// fetch("http://127.0.0.1:4523/m1/3576146-0-default/staff/comment/all", requestOptions)
//     .then(response => response.json())  // 解析JSON格式的响应
//     .then(data => displayData(data))
//     .catch(error => console.log('error', error));

axios.get("http://127.0.0.1:4523/m1/3576146-0-default/staff/comment/all", {
    headers: {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoxLCJwaG9uZSI6IjE5ODI5NjY5MzUwIiwibmFtZSI6Im1pem9yZSIsImlkIjoxLCJleHAiOjE2OTkzNjI1Mjh9.rrnU4zJeWfBx5BkBnCygXzuIu0zjMcakp0439frQbLM"
        // "User-Agent": "Apifox/1.0.0 (https://apifox.com)"
    }
})
.then(response => displayData(response.data))
.catch(error => console.log('error', error));
      
    function displayData(data) {
        var ordersContainer = document.getElementById('contents');
        ordersContainer.innerHTML = '';

        data.data.forEach(order => {
            var orderElement = document.createElement('div');
            orderElement.classList.add('order-container','flex-col', 'list-item' );
            
            // 时间
            var orderInfoElement = document.createElement('div');
            orderInfoElement.classList.add('flex-row', 'justify-between', 'items-center', 'group_2');
    
            var orderDateElement = document.createElement('div');
            orderDateElement.classList.add('flex-row', 'items-center', 'space-x-10');
    
            var orderImageElement = document.createElement('img');
            orderImageElement.classList.add('shrink-0', 'image');
            orderImageElement.src = "./picture/clock.png"; 
    
            var orderDateTextElement = document.createElement('span');
            orderDateTextElement.classList.add('font_4');
            orderDateTextElement.textContent = order.createTime; 
    
            orderDateElement.appendChild(orderImageElement);
            orderDateElement.appendChild(orderDateTextElement);
    
            orderInfoElement.appendChild(orderDateElement);
            orderElement.appendChild(orderInfoElement);

            //评价
            var orderPingjiaElement = document.createElement('div');
            orderPingjiaElement.classList.add('flex-row', 'justify-between',  'group_13');

            var orderPingElement = document.createElement('span');
            orderPingElement.classList.add('font_2y','text-wrapper_y','text_14y');
            orderPingElement.textContent=order.content;

            orderPingjiaElement.appendChild(orderPingElement);
            orderElement.appendChild(orderPingjiaElement);

            //总价 订单人 查看详情
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
                // window.location.href = './reviews _ details.html?orderId=' + '1721848915910721536'; // 根据实际数据结构调整
             window.location.href = './reviews _ details.html?orderId=' + order.orderCode;
            });

            viewDetailsElement.appendChild(viewDetailsTextElement);
            orderTotalElement.appendChild(viewDetailsElement);

            orderElement.appendChild(orderTotalElement);

            ordersContainer.appendChild(orderElement);
        });
    }
      

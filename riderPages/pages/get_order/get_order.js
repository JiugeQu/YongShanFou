function onClick() {
  location.href = '../../pages/delivering_order/delivering_order.html';
}

function onClick_1() {
  location.href = '../../pages/history_order/history_order.html';
}

function onClick_2() {
  location.href = '../../pages/personal_center/personal_center.html';
}

function acceptOrder(orderCode){
    var myHeaders = new Headers();
    myHeaders.append("token", "<token>");
    myHeaders.append("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
    var requestOptions = {
        method: 'PUT',
        headers: myHeaders,
        redirect: 'follow'
    };
    fetch("http://127.0.0.1:4523/m1/3671781-0-default/deliver/order/deliver/1720690177053032448", requestOptions)
        .then(response => response.text())
        .then(result => console.log(result))
        .catch(error => console.log('error', error));
    getOrder();
}

function getOrder(){
  // 发送请求获取菜品数据
  fetch("http://127.0.0.1:4523/m1/3671781-0-default/deliver/order/state/3", {
    method: "GET",
    headers: {
      'token': '<token>',
      'User-Agent': 'Apifox/1.0.0 (https://apifox.com)'
    },
  })
      .then(response => response.json())
      .then(data => {
        const container = document.getElementById('OrderInfo');
        container.innerHTML=""
        data.data.forEach(order => {
          container.innerHTML+=`
            <div class="flex-col section_4">
            <div class="flex-col group_2">
              <span class="self-start font">用膳否</span>
              <span class="self-end font_2 text_5">待接单</span>
            </div>
            <div class="flex-col self-stretch" id="OrderItem${order.code}">
            </div>
            <div class="flex-col group_4">
              <div class="divider"></div>              
              <div class="flex-row items-center view_2">
                <span class="font_4">期望送达:</span>
                <span class="font_2 text_6 ml-15">${order.expectArriveTime}</span>
              </div>             
              <div class="flex-row items-center view_3">
                <span class="font_4 text_7">详细地址：</span>
                <span class="font_2 ml-7">${order.address}</span>
              </div>              
            </div>
            <div class="flex-row  items-center view_3">
                <button class="font_5 self-center" type = "button" onclick = "acceptOrder(${order.code})">确认接单</button>
              </div>
          </div>     
       `
        });
        data.data.forEach(order => {
          const container = document.getElementById('OrderItem'+order.code);
          container.innerHTML=""
          order.orderDishes.forEach(item => {
            container.innerHTML+=`
            <div class="divider view"></div>
              <div class="flex-row items-center group_3">
                <div class="flex-col items-center">
                  <img class="image_4" src="${item.dishImg}" />
                </div>
                <div class="ml-14 flex-col items-center">
                  <span class="font_4">${item.dishName}</span>
                </div>
              </div>
              `
          })
        })
      })
      .catch(error => {
        console.error('Error:', error);
      });
}
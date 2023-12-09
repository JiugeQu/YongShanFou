function onClick() {
  location.href = '../../pages/get_order/get_order.html';
}

function onClick_1() {
  location.href = '../../pages/delivering_order/delivering_order.html';
}

function onClick_2() {
  location.href = '../../pages/personal_center/personal_center.html';
}

function getHistory(){
  fetch("http://127.0.0.1:4523/m1/3671781-0-default/deliver/order/history", {
    method: "GET",
    headers: {
      'token': '<token>',
      'User-Agent': 'Apifox/1.0.0 (https://apifox.com)'
    },
  })
      .then(response => response.json())
      .then(data => {
        const container = document.getElementById('OrderInfo');
        data.data.forEach(order => {
          const orderElement = document.createElement('div');
          orderElement.innerHTML=`
          <div class="flex-col section_4">
            <div class="flex-col group_2">
              <span class="self-start font">用膳否</span>
              <span class="self-end font_2 text_5">已送达</span>
            </div>
            <div class="flex-col self-stretch" id="OrderItem${order.code}">
            </div>
            <div class="flex-col group_4">
              <div class="divider"></div>              
              <div class="flex-row items-center view_2">
                <span class="font_4">实际送达:</span>
                <span class="font_2 text_6 ml-15">${order.realArriveTime}</span>
              </div>             
              <div class="flex-row items-center view_3">
                <span class="font_4 text_7">详细地址：</span>
                <span class="font_2 ml-7">${order.address}</span>
              </div>              
            </div>
          </div>
       `
          container.appendChild(orderElement);
        });
        data.data.forEach(order => {
          const container = document.getElementById('OrderItem'+order.code);
          container.innerHTML=""
          order.orderDishes.forEach(item => {
            // const itemElement = document.createElement('div');
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
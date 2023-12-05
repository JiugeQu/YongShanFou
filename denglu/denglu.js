axios.defaults.baseURL = 'http://127.0.0.1:4523/m1/3592222-0-default';

// 获取短信验证码
document.addEventListener('DOMContentLoaded', function() {  
   var button = document.getElementById('button_duanxin');  
   button.addEventListener('click', function() {  
      axios.post('/user/code', null, { 
         params: phone 
      })  
      .then(function (response) {  
         console.log(response.data.msg);
         document.getElementById("code").value = response.data.msg;   
      })  
      .catch(function (error) {  
         console.error(error);  
      });
   });  
});
  
// 登录
var role = sessionStorage.getItem('role'); // 从sessionStorage中取出role值
document.addEventListener('DOMContentLoaded', function() {  
   var button = document.getElementById('button_denglu');  
   button.addEventListener('click', function() {  
      var queryBody = {
         phone: document.getElementById('phone').value,
         code: document.getElementById('code').value,
         role: role
      };
      axios.post('/user/login', queryBody)
      .then(function (response) {  
         console.log(response.data); 
         // 如果响应中包含token，则保存到localStorage中  
         if (response.data.data) {  
            const token = response.data.data;  
            localStorage.setItem('token', token);  
         }else{
            console.log('no token');
         }
      })  
      .catch(function (error) {  
         console.error(error);  
      });
   });  
});
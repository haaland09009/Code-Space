

$('#techBox').select2 ({
    placeholder: '기술스택을 선택하세요.',
    maximumSelectionLength: 5
});

 // 이전 날짜 방지
 const setDateInput = () => {
     let endDateElement = document.getElementById('endDateBox');
     let currentDate = new Date();
     let endDatePos = new Date(currentDate.getTime() - currentDate.getTimezoneOffset() * 60000).toISOString().slice(0, -8);

     endDateElement.value = endDatePos.split('T')[0];

     endDateElement.setAttribute("min", endDatePos.split('T')[0]);
 };

 // 게시글 작성
 const writeBoard = () => {
     const projectStudy = document.querySelector("#projectStudyBox");
     const tech = document.querySelector("#techBox");
     const position = document.querySelector("#positionBox");
     const headCount = document.querySelector("#headCountBox");
     const period = document.querySelector("#periodBox");
     const endDate = document.querySelector("#endDateBox");

     const title = document.querySelector("#title");
     const content = document.querySelector("#content");

     if (projectStudy.value == "") {
         alert("모집 구분란을 선택해주세요.");
         projectStudy.focus();
         return false;
     }

     if (tech.value == "") {
         alert("기술 스택을 선택해주세요.");
         tech.focus();
         return false;
     }

     if (position.value == "") {
         alert("모집 포지션을 선택해주세요.");
         position.focus();
         return false;
     }

     if (headCount.value == "") {
         alert("모집 인원을 선택해주세요.");
         headCount.focus();
         return false;
     }

     if (period.value == "") {
         alert("진행 기간을 선택해주세요.");
         period.focus();
         return false;
     }
     if (endDate.value == "") {
         alert("모집 마감일을 선택해주세요.");
         endDate.focus();
         return false;
     }
     if (title.value.trim() == "") {
         alert("제목을 입력하세요.");
         title.focus();
         return false;
     }

     if (content.value.trim() == "") {
         alert("내용을 입력하세요.");
         content.focus();
         return false;
     }
     return true;
 }



window.addEventListener("DOMContentLoaded", function(){
 setDateInput();
});
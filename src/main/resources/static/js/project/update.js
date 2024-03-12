$('#techBox').select2 ({
    placeholder: '기술스택을 선택하세요.',
    maximumSelectionLength: 5,
   "language": {
          "noResults": function(){
              return "검색 결과가 없습니다.";
          },
          maximumSelected: function (e) {
                  var t = "최대 " + e.maximum + "개까지 선택하실 수 있습니다.";
                  /*e.maximum != 1 && (t += "s");*/
                  return t;
          }
      },
   escapeMarkup: function (markup) {
       return markup;
   }
});

$('#positionBox').select2 ({
    placeholder: '모집 포지션을 선택하세요.',
    maximumSelectionLength: 3,
   "language": {
          "noResults": function(){
              return "검색 결과가 없습니다.";
          },
           maximumSelected: function (e) {
                var t = "최대 " + e.maximum + "개까지 선택하실 수 있습니다.";
                /*e.maximum != 1 && (t += "s");*/
                return t;
        }
      },
   escapeMarkup: function (markup) {
       return markup;
   }
});
// 생성 시 옵션 설정
$( ".datePicker").datepicker({ minDate: 0});

$(".datepicker").datepicker({
    dateFormat: "yyyy-mm-dd",
    minDate: new Date(),
    maxDate: "+1M"
});
    $.datepicker.setDefaults({
        dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
        monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
        yearSuffix: '년',
        dateFormat: 'yy-mm-dd',
        showMonthAfterYear:true,
        constrainInput: true
    });


   // 게시글 수정
   const updateBoard = () => {
       const title = document.querySelector("#title");
       const content = document.querySelector("#content");
       const userId = sessionId;

       const projectStudy = document.querySelector("#projectStudyBox");
       const tech = document.querySelector("#techBox");
       const position = document.querySelector("#positionBox");
       const headCount = document.querySelector("#headCountBox");
       const period = document.querySelector("#periodBox");
       const endDate = document.querySelector("#endDateBox");


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


       if (userId == null) {
           location.href = "/user/loginPage";
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



  /* 토스트 에디터 불러오기 */
   const editor = new toastui.Editor({
        el: document.querySelector('#content'), // 에디터를 적용할 요소 (컨테이너)
        height: '500px',                        // 에디터 영역의 높이 값 (OOOpx || auto)
        initialEditType: 'markdown',            // 최초로 보여줄 에디터 타입 (markdown || wysiwyg)
        previewStyle: 'vertical',                // 마크다운 프리뷰 스타일 (tab || vertical)
//        initialValue: boardContent
    });


    // 폼 참조
    const updateForm = document.querySelector('#updateForm');

    // 에디터 내용을 폼의 hidden 필드에 할당하는 함수
    const setContentInputValue = () => {
        const contentInput = document.querySelector('#contentInput');
        contentInput.value = editor.getHTML(); // 또는 editor.getHTML()을 사용하여 HTML 내용을 가져올 수 있습니다.
    }

    // 폼 제출 전에 에디터 내용을 갱신
    updateForm.addEventListener('submit', function () {
        setContentInputValue();
    });
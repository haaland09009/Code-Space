
    /* 해시태그 입력창 */
    var input = document.querySelector('input[name="tags"]');
    var whitelist = ["백엔드","프론트엔드","안드로이드","Java", "JavaScript",
                 "Spring", "Spring Boot", "React","Vue", "C", "C++", "Python", "NodeJS",
                  "Swift", "Kotlin", "Flutter", "TypeScript", "자바","스프링", "HTML","CSS","AWS","딥러닝","AI","블로그","노션"];
    var tagify = new Tagify(input, {
        whitelist: whitelist,
        maxTags: 5,
        dropdown: {
            maxItems: 20,
            classname: "tags-look",
            enabled: 0,
            closeOnSelect: false
        }
    })

  /*  게시글 작성  */
  const writeQna = () => {

    const topic = document.querySelector("#topicBox");
    const title = document.querySelector("#title");
    const content = document.querySelector("#content");

    if (topic.value == "") {
        alert("토픽을 선택해주세요.");
        topic.focus();
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



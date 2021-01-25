$(function(){
	question_import.initial();
});

var question_import={
		initial : function initial() {
			this.prepareUploadify();
			this.questionDataProcess();
			this.bindChangeFileType();
		},
		prepareUploadify : function prepareUploadify(){
			setTimeout(function(){
				$("#uploadify").uploadify({
			    	'debug'	 : false,
					'buttonText'	: '点击上传附件',
					'buttonCursor'	: 'pointer',
					'uploader'	 : document.getElementsByTagName('base')[0].href + 'secure/upload-uploadify/',
					'queueID': 'fileQueue',
					'swf'	 : document.getElementsByTagName('base')[0].href + 'resources/js/uploadify/uploadify.swf',
					'multi'	 : false,
					'auto'	 : true,
					'height' : '26',
					'width'	 : '160',
					'requeueErrors'	: false,
					'fileSizeLimit'	: '20480', // expects input in kb
					'cancelImage'	: document.getElementsByTagName('base')[0].href + 'resources/js/uploadify/cancel.png',
					removeCompleted : true,
					overrideEvents:['onSelectError','onDialogClose'],
					onUploadComplete: function(file) {
					},
					onUploadSuccess : function(file, data, response) {  
						$('#div-file-list').html('<a class=\'file-name\'>' 
								+ file.name 
								+ '</a><input type=\'hidden\' value=\''
								+ data + '\' />'); //+ file.name + '\' />');
			        },
					onSelectError: function(file,errorCode,errorMsg) {
						if(errorCode==-110){
							util.notify("只能上传20M以下的文件。");
							return false;
						}
					},
					onUploadError: function(file,errorCode,errorMsg, errorString) {
						util.error(errorMsg);
					}
			    });
			},2);
		},

		questionDataProcess : function questionDataProcess(){
			$("#from-question-import").submit(function(){
                var question_entity = new Object();
                var fieldId = 0;
                //判断文件类型  1、word  2、excel
                var fileType = $("#file-type select").val();
                //检查题库类型
                if(fileType==1){
                    fieldId = $("#field-select > option:selected").attr("value");

                    if( "undefined" == fieldId || null == fieldId){
                        $(".form-message").text("");
                        $("#field-select-msg .form-message").text("请选择题库分类信息");
                        $("#field-select-msg .form-message").addClass("has-error");
                        return false;
                    }

                    //检查知识点
                    if (!question_import.checkKnowledge()){
                        return false;
                    }
                    //获取知识点
                    var pointList = new Array();
                    var pointOpts = $("#point-to-select option");
                    for (var i = 0; i < pointOpts.length; i++) {
                        pointList.push($(pointOpts[i]).attr("value"));
                    }
                    question_entity.pointList = pointList;

                }else if(fileType==2){//excel文件
                    fieldId = $(".upload-question-group select").val();
                    if(0 == fieldId){
                        $(".form-message").text("");
                        $(".upload-question-group .form-message").text("请选择题库分类");
                        $(".upload-question-group .form-message").addClass("has-error");
                        return false;
                    }

                }

                //检查文件上传
                if (null == $("#div-file-list").find("input").val() || "undefined" == $("#div-file-list").find("input").val()){
                    $(".form-message").text("");
                    $(".file-form-line .form-message").addClass("has-error");
                    $(".file-form-line .form-message").text("请上传有效文件");
                    alert("上传文件无效:"+$("#div-file-list").find("input").val());
                    return false;
                }

				var filePath = $("#div-file-list").find("input").val().split('\\').pop();
                var fileName = $("#div-file-list").find(".file-name").text();
				// alert(filePath);
				// alert($("#from-question-import").attr("action") + "/" + $(".upload-question-group select").val())

                question_entity.filePath = filePath;
                question_entity.name = fileName;
                question_entity.fieldName = fieldId;
				$.ajax({
					headers : {
						'Accept' : 'application/json',
						'Content-Type' : 'application/json'
					},
					type : "POST",
					url : $("#from-question-import").attr("action") + "/" + fieldId,
					// data : filePath,
					data : JSON.stringify(question_entity),
					success : function(message, tst, jqXHR) {
						if (!util.checkSessionOut(jqXHR))
							return false;
						if (message.result == "success") {
							util.success("导入成功", function() {
								$("#submit-div .form-message").text(message.messageInfo);
								document.location.href = document.getElementsByTagName('base')[0].href + util.getCurrentRole() + '/question/question-list'
							});
						} else {
							util.error("操作失败请稍后尝试:" + message.result);
							$("#submit-div .form-message").text(message.messageInfo);
							$("#btn-add-submit").removeAttr("disabled");
						}
					},
					error : function(jqXHR, textStatus) {
						util.error("操作失败请稍后尝试");
						$("#btn-add-submit").removeAttr("disabled");
					}
				});
				return false;
			});
		},

		/**
		 * 检查知识点
		 */
		checkKnowledge : function checkKnowledge() {
			var result = true;
            $(".form-message").text("");
			if ($("#point-to-select option").length == 0) {
				// $(".question-knowledge .form-message").text("该试题至少对应一个知识点");
                $("#knowledge-select-msg .form-message").text("该试题至少对应一个知识点。");
				$("#point-to-select").addClass("has-error");
				result = false;
			} else if ($("#point-to-select option").length > 4) {
				// $(".question-knowledge .form-message").text("知识点数量不应该超过4个");
				$("#knowledge-select-msg .form-message").text("知识点数量不应该超过4个");
				$("#point-to-select").addClass("has-error");
				result = false;
			}

			return result;

		},
        bindChangeFileType : function changeFileType() {
            $("#file-type select").change(function(){
                if (1== $(this).val()) {
                    $(".upload-question-group").hide();
                    $(".question-knowledge").show();
                } else if (2 == $(this).val()) {
                    $(".upload-question-group").show();
                    $(".question-knowledge").hide();
                }

            });
        }
    };
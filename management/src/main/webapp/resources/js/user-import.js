$(function(){
	user_import.initial();
});

var user_import={
		initial : function initial() {
			this.prepareUploadify();
			this.userDataProcess();
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
		userDataProcess : function userDataProcess(){
			$("#form-user-import").submit(function(){
				var user = new Object();
				var fieldId = 0;


				//检查文件上传
				if (null == $("#div-file-list").find("input").val() || "undefined" == $("#div-file-list").find("input").val()){
					$(".form-message").text("");
					$(".file-form-line .form-message").addClass("has-error");
					$(".file-form-line .form-message").text("请上传有效文件");
					// alert("上传文件无效:"+$("#div-file-list").find("input").val());
					return false;
				}

				var filePath = $("#div-file-list").find("input").val().split('\\').pop();
				var fileName = $("#div-file-list").find(".file-name").text();
				// alert(filePath);
				// alert($("#from-question-import").attr("action") + "/" + $(".upload-question-group select").val())
				alert($("#form-user-import").attr("action"));
				user.filePath = filePath;
				user.fileName = fileName;
				//user.fieldName = fieldId;
				$.ajax({
					headers : {
						'Accept' : 'application/json',
						'Content-Type' : 'application/json'
					},
					type : "POST",
					url : $("#form-user-import").attr("action"),
					// data : filePath,
					data : JSON.stringify(user),
					success : function(message, tst, jqXHR) {
						if (!util.checkSessionOut(jqXHR))
							return false;
						if (message.result == "success") {
							util.success("导入成功", function() {
								$("#submit-div .form-message").text(message.messageInfo);
								document.location.href = document.getElementsByTagName('base')[0].href + util.getCurrentRole() + '/user/student-list'
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
		}
    };

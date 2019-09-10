//-------------------------绘图信息展示部分-----------------------------
var c_dis;
var cxt_dis;
function getDrawingMessageFromServer(mesContentTrans_Str){//messageprocess.js
	
	//页面加载后再获取c_dis
	c_dis=document.getElementById("drawingCanvas_guesser");
	cxt_dis=c_dis.getContext("2d");
	
    /*var mesContentTrans={ //绘图信息
        	"colorIndexTrans":penColorIndex,
      		"thicknessTrans":penThickness,
      		"pointTrans":[{"x":v,"y":v}]
     	};*/
	//document.getElementById("pointTest").innerHTML+="mesContentTrans_Str::"+mesContentTrans_Str+"<br>";
    var mesContentTrans=JSON.parse(mesContentTrans_Str);
	
    if(mesContentTrans.colorIndexTrans!=-1){//-1代表清屏
		cxt_dis.strokeStyle=getColor_dis(mesContentTrans.colorIndexTrans);
		cxt_dis.lineWidth=mesContentTrans.thicknessTrans;
		
		//document.getElementById("pointTest").innerHTML+="mesContentTrans.pointTrans::"+mesContentTrans.pointTrans+"<br>";
		/* 测试输出mesContentTrans.pointTrans的内容//Object
		 * test2=JSON.stringify(mesContentTrans.pointTrans);//String
	     * alert("test2->"+typeof test2);//string
	     * document.getElementById("pointTest").innerHTML+="test::"+test2+"<br>";
	     */
	
		cxt_dis.beginPath();
		cxt_dis.moveTo(mesContentTrans.pointTrans[0].x,mesContentTrans.pointTrans[0].y);
		for(i in mesContentTrans.pointTrans){
			if(i>0){
				xp=mesContentTrans.pointTrans[i].x;//number
				yp=mesContentTrans.pointTrans[i].y;
				//document.getElementById("pointTest").innerHTML+="【i="+i+",x="+xp+",y="+yp+"】";
				cxt_dis.lineTo(xp,yp);
			}
		}
		cxt_dis.stroke();
		
    }else{//清屏
    	clearCanvas_dis();
    }
}
function getColor_dis(index){//得到一个颜色的RGB
	var drawcolor= new Array("#FF0000","#FF8800","#FFFF00","#00FF00","#00FFFF","#0000FF","#FF00FF","#888888","#000000","#FFFFFF");
	//alert("color:"+drawcolor[index]);
	return drawcolor[index];
}
function clearCanvas_dis(){//清屏
	cxt_dis.clearRect(0,0,c_dis.width,c_dis.height);
}
//-------------------------绘图信息展示部分展示-----------------------------
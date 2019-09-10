//-------------------------绘图部分-----------------------------
var c;//canvas
var cxt;//context
var penColorIndex;//当前画笔的颜色数组下标
var penThickness;//当前画笔的lineWidth值
/**
*JSON messageTrans={
*		"mesTypeTrans":x,
*		"mesContentTrans":JSON，待传输数据
		    {
			    "colorIndexTrans":8,
			    "thicknessTrans":2,
			    "pointTrans":[{k,v}]    	
		    }
*     }
*/
var pointDrawing=[];//绘图坐标信息

function drawingPageForPainterOnload(){//绘图页面初始化
	c=document.getElementById("drawingCanvas_painter");
	cxt=c.getContext("2d");
	//给canvas元素添加鼠标事件
	c.onmousedown=startDrawing;
	c.onmouseup=sendDrawingPoint;
	c.onmouseout=stopDrawing;
	c.onmousemove=drawing;
	
	onloadColor();//填充canvas元素对应颜色
	chooseColor("#000000");//设置笔触粗细
	penColorIndex=8;
    penThickness=2; 
}

function onloadColor(){	
	var drawcolorname=new Array("red","orange","yellow","green","teal","blue","purple","gray","black","white");
	for(i=0;i<drawcolorname.length;i++){
		/**var con="\""+drawcolorname[i]+"\"";
		 * alert(con);
		 * var cc=document.getElementById(con);
		 * drawcolorname[i]本质即String，直接传值给getElementById(String)即可,下drawcolor[i]同
		 * 若强行加上"",反倒改变了String的内容（将xx变为"xx")，无法获取到相应元素
		 * var x="xxx"/'xxx'的本质依旧是标明xxx是个String而非Object或是其他
		 */
		var cc=document.getElementById(drawcolorname[i]);
		//alert(cc.className);
		var ccxt=cc.getContext("2d");
	    ccxt.fillStyle=getColor(i);
	    ccxt.beginPath();  
	    ccxt.fillRect(0,0,c.width,c.height);  
	    ccxt.closePath(); 		
	}
}

//设置标记，只有mousedown+mousemove为作画，排除未按下鼠标而在canvas元素区move的情况
var flag=false;
var pdi=0; //pointDrawing的数组下标开始值，0为startDrawing()中的moveTo
//按下鼠标
function startDrawing(e){
	//alert("e is"+e);
	//alert("c is"+c);
	flag=true;
	//设置新的绘图路径，否则默认所有子路径为同一路径，无法变换颜色，所有路径将为同一颜色	
	cxt.beginPath();
	//每一笔路（每次按下鼠标）径为一新路径，初始值定位到鼠标按下的位置
	//canvas：border:10px
	cxt.moveTo(e.pageX-c.offsetLeft-10,e.pageY-c.offsetTop-10);//e.clientX  e.pageX offsetTop/offsetY
	//var x=e.pageX-c.offsetLeft;//number 
	//alert(typeof x);
	//--------------------1、确定初始位置--------------------------
	setDrawingPoint(e.pageX-c.offsetLeft-10,e.pageY-c.offsetTop-10,pdi);
	pdi=1;
}
function sendDrawingPoint(){
	flag=false;
	//---------------------3、结束标记,发送--------------------------
	pdi=0;//恢复初始值
	setDrawingMessage(true);
}
function stopDrawing(){
	flag=false;
}

function drawing(e){
	if(flag){
		var x=e.pageX-c.offsetLeft-10; //canvas：border:10px
		var y=e.pageY-c.offsetTop-10;
		cxt.lineTo(x,y);
		cxt.stroke(); //.closePath()会建立闭环
		//---------------------2、每次绘画位置--------------------------
		setDrawingPoint(x,y,pdi);
		pdi++;
	}
}

function setColor(index){//设置选中的颜色
	penColorIndex=index;
	var color=getColor(index);
	cxt.strokeStyle=color;
	chooseColor(color);//通过笔触颜色的改变标明当前选中颜色已改变
}
function getColor(index){//得到一个颜色的RGB
	var drawcolor= new Array("#FF0000","#FF8800","#FFFF00","#00FF00","#00FFFF","#0000FF","#FF00FF","#888888","#000000","#FFFFFF");
	return drawcolor[index];
}
function chooseColor(color){//根据传入颜色参数设置笔触粗细
	var pens= new Array("thin_pen","medium_pen","wide_pen");
	for(i=0;i<pens.length;i++){
		var cp=document.getElementById(pens[i]);
		var cpxt=cp.getContext("2d");		    
	    cpxt.fillStyle=color; //初始化为黑色
	    cpxt.beginPath();
	    cpxt.arc(12.5,12.5,i*2+2,0,Math.PI*2,true);//10相对于当前canvas元素，也可通过c.offsetLeft获取当前元素的相对位置
	    //cpxt.closePath();
	    cpxt.fill();		
	}
}

//var choseThickness;用于标记选择状态
function setThickness(thickness){
	penThickness=thickness;
	cxt.lineWidth=penThickness;
	//需要将当前笔触标签设置为已选中状态，将之前的标签设置为默认样式
}
function clearCanvas(){
	cxt.clearRect(0,0,c.width,c.height);
	//---------------------4、清屏信息也需要发送，属于单独发送的信息--------------------------
	setDrawingMessage(false);//发送绘图信息
}
//-------------------------绘图部分结束-----------------------------
//-------------------------数据传输-----------------------------

//参数xValue,yValue为相对于canvas的位置
function setDrawingPoint(xValue,yValue,i){ 	
	if(flag){//drawing
		//document.getElementById("test").innerHTML+="["+xValue+","+yValue+"]"+"i is "+i;
	    //var x=x+"";
		pointDrawing[i]={x:xValue,y:yValue}; 
	}
}
//组装待发送信息
function setDrawingMessage(f){
    var mesContentTrans={ 
		"colorIndexTrans":penColorIndex,
 	    "thicknessTrans":penThickness,
 	    "pointTrans":""
 	    };
    
    //alert("pointDrawing->"+typeof pointDrawing);//Object
    mesContentTrans.pointTrans=pointDrawing;//Object
    //document.getElementById("pointTest").innerHTML+="mesContentTrans.pointTrans->"+mesContentTrans.pointTrans+"<br>";
    
    /* 测试输出pointDrawing（即mesContentTrans.pointTrans）的内容
     * test1=JSON.stringify(mesContentTrans.pointTrans);//String
     * document.getElementById("pointTest").innerHTML+="test1->"+test1+"<br>";
     */
    pointDrawing=[];
	if(f){
	}else{
		mesContentTrans.colorIndexTrans=-1;	//清屏
	}
	var mesContentTrans_Str=JSON.stringify(mesContentTrans);//发送drawingMessage
	sendDrawingMessage(mesContentTrans_Str);//messageprocess.js
	
	/*传输point数据信息测试
	 *document.getElementById("pointTest").innerHTML+=mesContentTrans_Str+"\r\n";
	 *getDrawingMessageFromServer(mesContentTrans_Str);
	 */
}
//-------------------------数据传输结束-----------------------------
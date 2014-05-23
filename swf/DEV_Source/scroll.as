generarScrollEasyn = function (nivel, clip, anchoScroll, suavizado) {
	if (clip != undefined) {
		var anchoScroll = anchoScroll;
		var div = suavizado;
		if (div != undefined) {
			if (div<1) {
				div = 1;
			} else {
				div = suavizado;
			}
		} else {
			div = 10;
		}
		var ruta = nivel;
		var altoScroll = Math.floor(ruta.mcMask._height);
		var mask_top = clip._y;
		var mask_height = ruta.mcMask._height;
		var mc_heigth = clip._height;
		if (mc_heigth<mask_height or mc_heigth == mask_height) {
			// el campo de texo es ma corto que el largo de la mascara
		} else {
			ruta.attachMovie("mcContScroll", "mcContScroll", _root.mcCont);
			ruta.mcContScroll.attachMovie("fondo", "fondo", ruta.mcContScroll);
			with (ruta.mcContScroll) {
				ruta.mcContScroll._x = clip._x+clip._width+5;
				fondo._height = altoScroll;
				attachMovie("barrita", "barra", 1);
				barra._height = 50;
				barra.inicioY = barra._y;
				barra.finalY = barra._y+ruta.mcMask._height-barra._height;
				barra.inicioX = barra.finalX=barra._x;
				var scroll_top = barra._y;
				var scroll_buttom = Math.floor(ruta.mcMask._height-barra._height-3);
				if (anchoScroll>3) {
					fondo._width = anchoScroll;
					barra._width = anchoScroll;
				} else {
					fondo._width = 3;
					barra._width = 3;
				}
				barra.onPress = function() {
					this.gotoAndStop(2);
					this.startDrag(0, this.inicioX, this.inicioY, this.finalX, this.finalY);
					mouse_drag = true;
					this._alpha = 100;
					this.onEnterFrame = function() {
						var mcAlto = clip._height;
						if (mcAlto<mask_height) {
							return;
						}
						if (mouse_drag) {
							var mk = ruta.mcMask;
							if (mot<(-1)*(clip._height-mk._height)) {
								mot = Math.round((clip._height-mk._height))*-1;
							}
							var pos = Math.floor(mask_top-(mcAlto-mask_height)*(barra._y-scroll_top)/(scroll_buttom-scroll_top));
							var posY = pos-clip._y;
							clip._y -= Math.round((int(mot)-posY)/div);
						}
					};
				};
				barra.rollOut = function() {
					this._alpha = 100;
					this.gotoAndStop(1);
				};
				barra.onRollOver = function() {
					this._alpha = 100;
					this.gotoAndStop(2);
				};
				barra.onRelease = function() {
					this.stopDrag();
					this.gotoAndStop(1);
					//mouse_drag = false;
					//delete this.onEnterFrame;
				};
				barra.onReleaseOutside = barra.onRelease;
			}
		}
	} else {
		trace(nivel);
		nivel.createTextField("my_txt", 1, 100, 100, 300, 100);
		nivel.my_txt.multiline = true;
		nivel.my_txt.wordWrap = true;
		var my_fmt:TextFormat = new TextFormat();
		my_fmt.color = 0xFF0000;
		my_fmt.underline = true;
		my_fmt.font = "Verdana";
		my_fmt.size = 10;
		nivel.my_txt.htmlText = "No se ha podido detectar el CLIP CONTENEDOR";
		nivel.my_txt.setTextFormat(my_fmt);
		nivel.my_txt._x = nivel._x;
		nivel.my_txt._y = nivel._y;
	}
};

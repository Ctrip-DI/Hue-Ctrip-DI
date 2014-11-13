   var Loading = function (element, options) {
        this.options = options;
        this._element = $(element);
        this._uid = 'loading-'+new Date().getTime()+(Math.random()*1e10).toFixed(0);
    };

    Loading.prototype = {

        constructor: Loading, 

        show: function () {
            var that = this, mask = '', icon = '';

            if( that._element.find('.loading').length ) return

            if( that._element.css('position')==='static' ){
                that._element.css('position','relative');
            }

            var s = '.loading-mask{background-color:#000;opacity:0.30;filter:alpha(opacity=30);}.loading{display:inline-block;*display:inline;*zoom:1;top:50%;left:50%;padding:20px 40px 16px;background-color:#fff;text-align:center;color:#666;font-size:12px;border:1px solid #888;border-radius:2px;box-shadow:2px 2px 0 #999;}.loading > img{margin:0 10px 0 0;position:relative;top:-2px;}';

            s = s + that.options.style;

            s = '<style class="'+that._uid+'">'+s.replace(/(\s*)([^\{\}]+)\{/g,function(a,b,c){return b+c.replace(/([^,]+)/g,'.'+that._uid+'$1')+'{';})+'</style>';

            if( that.options.mask ){
                mask = '<div class="loading-mask '+that._uid+'"></div>';
            }

            if( typeof that.options.icon==='boolean' && that.options.icon ){
                icon = '<img src="/sparksql/static/art/loads.gif">';
            }

            if( typeof that.options.icon==='string' && that.options.icon ){
                icon = '<img src="'+that.options.icon+'">';
            }

            that._element.append( s+mask+'<div class="loading '+that._uid+'">'+icon+'<span>'+that.options.text+'</span></div>' );

            var w = $('.'+that._uid+'.loading')[0].offsetWidth;
            var h = $('.'+that._uid+'.loading')[0].offsetHeight;

            $('.'+that._uid+'.loading-mask').css({
                'position': that.options.position,
                'z-index': that.options.zindex-1,
                'top': that.options.maskOffset.top,
                'right': that.options.maskOffset.right,
                'left': that.options.maskOffset.left,
                'bottom': that.options.maskOffset.bottom
            });

            if( that.options.position==='fixed' ){
                h = 0;
                w = 0;
            }else if( that.options.position==='absolute' ){
                h = h/2*(-1);
                w = w/2*(-1);
            }

            $('.'+that._uid+'.loading').css({
                'position': that.options.position,
                'z-index': that.options.zindex,
                'margin-top': h+that.options.offsetTop, 
                'margin-left': w+that.options.offsetLeft 
            });
        }, 

        hide: function (e) {
            e && e.preventDefault();
            if( this.options.fadeout ){
                $('.'+this._uid).fadeOut(400, function(){
                    $(this).remove();
                });
            }else{
                $('.'+this._uid).remove();
            }
        }
    };

    var old = $.fn.loading;

    $.fn.loading = function (option) {
        return this.each(function () {
            var $this = $(this), 
                data = $this.data('loading'), 
                options = $.extend({}, $.fn.loading.defaults, $this.data(), typeof option == 'object' && option);

            if (!data) $this.data('loading', (data = new Loading(this, options)));
            if (typeof option == 'string') 
                data[option]();
            else if 
                (options.show) data.show();
        });
    };

    $.fn.loading.defaults = {
        show: true,
        mask: true,
        fadeout: false,
        zindex: 1000,
        offsetTop: 0,
        offsetLeft: 0,
        maskOffset: {
            top: 0,
            right: 0,
            left: 0,
            bottom: 0
        },
        position: 'absolute', 
        icon: true,
        style: '',
        text: '正在加载中...'
    };

    $.fn.loading.Constructor = Loading;

    $.fn.loading.noConflict = function () {
        $.fn.loading = old;
        return this;
    };


function loading(){ 
	container.html('<div class="docs-loading"><img src="/sparksql/static/art/loads.gif"><br>正在加载中...</div>');
}
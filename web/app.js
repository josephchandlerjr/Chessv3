// ---------------------------------------------------
// Modal Chess
// ---------------------------------------------------
function Chess() {
	self = this;
	this.fromRow = undefined;
	this.fromCol = undefined;
	this.toRow = undefined;
        this.toCol = undefined;
	this.squares = [];
	this.logTo = function(event){
		event.preventDefault();
		self.toRow = $(this).data('row');
		self.toCol = $(this).data('col');
		self.makeMove();
	};

	this.logFrom = function(event){
		event.preventDefault();
		self.fromRow = $(this).data('row');
		self.fromCol = $(this).data('col');

	};
	this.init = function() {
		var white = 'board-white-square';
		var black = 'board-black-square';
		var color = white;
		var chessBoard = $('.chess-board');

		for(var i=0 ; i < 8; i++){
			color = color == white ? black : white; 
			for(var j=0 ; j < 8; j++){
				var square = $('<div data-row='+i+' data-col='+j+'></div>');
				square.addClass(color);
				square.addClass('board-square');
				chessBoard.append(square);
				this.squares.push(square);
				color = color == white ? black : white; 
			}
		}
		var boardSquare = $('.board-square');
		boardSquare.on('mousedown',this.logFrom);
		boardSquare.on('mouseup',this.logTo);
		this.updateBoard();

	};
	this.updateBoard = function(){
		$.ajax('/Chess/game.do',{
		type : 'GET',
		dataType: 'json',
		success: function(result){
			var pieces = result["boardRepr"].split(" ");
			for(var i=0; i<self.squares.length; i++){
				self.removePiece(self.squares[i]);
				self.addPiece(self.squares[i],pieces[i]);
			}
			var blackLabel = $('.black-label');
			var whiteLabel = $('.white-label');
			if(result["whiteToMove"]){
				whiteLabel.text("your move");
				blackLabel.text("waiting");
			}
			if(result["blackToMove"]){
				blackLabel.text("your move");
				whiteLabel.text("waiting");
			}
			if(result["whiteInCheck"])
				whiteLabel.text("In Check!");
			if(result["blackInCheck"])
				blackLabel.text("In Check!");
			if(result["whiteHasWon"]){
				whiteLabel.text("You Win!");
				blackLabel.text("You Lose :( ");
			}
			if(result["blackHasWon"]){
				blackLabel.text("You Win!");
				whiteLabel.text("You Lose :( ");
			}
		}});
	};
	this.makeMove = function(){
		$.ajax('/Chess/game.do',{
		type: 'POST',
		data: {"fromRow":self.fromRow,
		        "fromCol":self.fromCol,
			"toRow":self.toRow,
			"toCol":self.toCol},
		success: function(){
			self.updateBoard();
		}
		});
	};
	/* removePiece takes a DOM element to remove image from */
	this.removePiece = function(el){
		var classList = el.attr('class').split(/\s+/);
		$.each(classList, function(index,item){
			if(item.startsWith('piece')){
				el.removeClass(item);
				return;
			}
		});
	};
	/* addPiece takes a DOM element to add image to, and class name */
	this.addPiece = function(el,name){
		el.addClass("piece-"+name);
	}

}

// ---------------------------------------------------
// Document Ready
// ---------------------------------------------------

$(document).ready(function(){
	var chess = new Chess();
	chess.init();
	$('.play-chess').on('click',function(event){
		event.preventDefault();
		$('.chess-board').addClass('is-visible');
		$('.modal-overlay').addClass('is-visible');
		});
	$('.modal-overlay').on('click',function(event){
		$('.chess-board').removeClass('is-visible');
		$('.modal-overlay').removeClass('is-visible');
	});

	$('button.new-game').on('click',function(event){
		event.preventDefault();
		console.log('click me long time');});

});


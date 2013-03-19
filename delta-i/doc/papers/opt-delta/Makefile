FILE=paper

pdf:
	latex ${FILE}.tex
	bibtex paper
	latex ${FILE}.tex
# 	echo '******************************'
# 	latex ${FILE}.tex | grep -E '(arning|full)'
# 	echo '******************************'
	dvips -t letter -o ${FILE}.ps ${FILE}.dvi
	ps2pdf -dCompatibilityLevel=1.3 -dSubsetFonts=true -dEmbedAllFonts=true ${FILE}.ps ${FILE}.pdf

dvi:
	latex $(FILE).tex

clean:
# 	 TODO: better way to write this?
	-@(touch foo.dvi)
	-@(find . -name "*.dvi" | xargs rm)
#	-@(touch foo.ps)
#	-@(find . -name "*.ps" | xargs rm)
	-@(rm "paper.pdf")
	-@(touch foo.aux)
	-@(find . -name "*.aux" | xargs rm)
	-@(touch foo.blg)
	-@(find . -name "*.blg" | xargs rm)
	-@(touch foo.log)
	-@(find . -name "*.log" | xargs rm)
	-@(touch foo.bbl)
	-@(find . -name "*.bbl" | xargs rm)
	-@(touch foo.tex~)
	-@(find . -name "*~" | xargs rm)

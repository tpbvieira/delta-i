FILES := \
proposal.tex \

dvi:
	latex proposal.tex
	dvips -t letter -o proposal.ps proposal.dvi

pdf:
	latex proposal.tex
	bibtex proposal
	latex proposal.tex
	latex proposal.tex
	dvips -t letter -o proposal.ps proposal.dvi
	ps2pdf -dCompatibilityLevel=1.4 -dSubsetFonts=true -dEmbedAllFonts=true proposal.ps proposal.pdf

clean:
# 	 TODO: better way to write this?
	-@(touch foo.dvi)
	-@(find . -name "*.dvi" | xargs rm)
	-@(touch foo.ps)
	-@(find . -name "*.ps" | xargs rm)
	-@(touch foo.pdf)
	-@(find . -name "*.pdf" | xargs rm)
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

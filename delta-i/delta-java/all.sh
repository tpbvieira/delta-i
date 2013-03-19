#!/bin/bash

./BSTdelta.sh > bstdelta
./BSTstandard.sh > bststandard
./BSTnative.sh > bstnative
./treemapnative.sh > treemapnative
./treemapdelta.sh > treemapdelta
./treemapstandard.sh > treemapstandard

exit 0

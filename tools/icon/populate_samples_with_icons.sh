#!/bin/bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
for f in $DIR/../../samples/*; do 
	echo "populate_with_icons $f"
	bash $DIR/populate_with_icons.sh ppp-icon.svg $f
done
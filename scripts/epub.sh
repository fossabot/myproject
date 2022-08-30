#!/bin/bash
pandoc src/docs/metadata.yml src/docs/*.md --resource-path=src/docs/ --toc --toc-depth=2 -t epub3 -o target/DemoApplication-doc-1.0.0-SNAPSHOT.epub

ASSETS_DIR=app/src/main/assets

CSV=$(ASSETS_DIR)/products.csv

all: $(CSV)

$(CSV): products.ods
	bin/ods2csv.py products.ods $(ASSETS_DIR)

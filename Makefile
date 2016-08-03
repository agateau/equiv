ASSETS_DIR=app/src/main/assets

CSV=$(ASSETS_DIR)/products.csv

all: $(CSV)

$(CSV): products.ods
	bin/ods2csv.py --skip-row 1 --skip-column 6 products.ods $(ASSETS_DIR)

clean:
	rm -f $(CSV)

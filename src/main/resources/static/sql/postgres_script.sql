CREATE TABLE "agents" (
  "id" serial NOT NULL,
  "code" varchar(10) NOT NULL,
  "name" varchar(50) NOT NULL,
  "search_url" varchar(200) NOT NULL,
  "is_deleted" boolean NOT NULL default false,
  CONSTRAINT agents_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);

CREATE TABLE "input_styles" (
  "attribute_id" bigint NOT NULL,
  "value_id" bigint NOT NULL,
  CONSTRAINT input_styles_pk PRIMARY KEY ("attribute_id", "value_id")
) WITH (
OIDS=FALSE
);


CREATE TABLE "attributes" (
  "id" serial NOT NULL,
  "attribute" varchar(50) NOT NULL,
  CONSTRAINT attributes_pk PRIMARY KEY ("id")
) WITH (
OIDS = FALSE
);

CREATE TABLE "values" (
  "id" serial NOT NULL,
  "value" varchar(50) NOT NULL,
  CONSTRAINT values_pk PRIMARY KEY ("id")
) WITH (
OIDS = FALSE
);

CREATE TABLE "placeholders" (
  "id" serial NOT NULL,
  "value" varchar(50) NOT NULL,
  CONSTRAINT placeholders_pk PRIMARY KEY ("id")
) WITH (
OIDS = FALSE
);


CREATE TABLE "products" (
  "id" serial NOT NULL,
  "name" varchar(100) NOT NULL,
  "image" varchar(200) ,
  "visit_count" integer ,
  "rating" numeric(4,2) ,
  "agent_count" integer,
  CONSTRAINT products_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "products_agents" (
  "id" INTEGER NOT NULL PRIMARY KEY ,
  "product_id" bigint NOT NULL,
  "agent_id" bigint NOT NULL,
  "price" DECIMAL NOT NULL,
  "url" varchar NOT NULL
) WITH (
OIDS=FALSE
);

CREATE TABLE "ignored_tags" (
  "id" serial NOT NULL,
  "tag" varchar(50) NOT NULL,
  CONSTRAINT ignored_tags_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);

CREATE TABLE "removed_tags" (
  "id" serial NOT NULL,
  "tag" varchar(50) NOT NULL,
  CONSTRAINT removed_tags_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);

CREATE TABLE "format_tags" (
  "id" serial NOT NULL,
  "tag" varchar(50) NOT NULL,
  CONSTRAINT format_tags_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);

CREATE TABLE "ignored_words" (
  "id" serial NOT NULL,
  "word" varchar(50) NOT NULL,
  CONSTRAINT ignored_words_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "product_specifics" (
  "id" serial NOT NULL,
  "code" varchar(10) NOT NULL,
  "text" varchar(20) NOT NULL,
  CONSTRAINT product_specifics_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);


CREATE TABLE "specific_details" (
  "id" serial NOT NULL,
  "specific_id" bigint NOT NULL,
  "possible_text" varchar(50) NOT NULL,
  CONSTRAINT specific_details_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "crawling_requires" (
  "id" serial NOT NULL,
  "code" varchar(10) NOT NULL,
  "text" varchar(20) NOT NULL,
  CONSTRAINT crawling_requires_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "require_terms" (
  "id" serial NOT NULL,
  "require_id" bigint NOT NULL,
  "term" varchar(50) NOT NULL,
  CONSTRAINT require_terms_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);


CREATE TABLE "require_formats" (
  "id" serial NOT NULL,
  "require_id" bigint NOT NULL,
  "format" varchar(100) NOT NULL,
  CONSTRAINT term_formats_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);

CREATE TABLE "agent_rules" (
  "agent_id" bigint NOT NULL,
  "require_id" bigint NOT NULL,
  "format" varchar NOT NULL,
  "rule_index" integer NOT NULL,
  CONSTRAINT agent_rules_pk PRIMARY KEY ("agent_id", "require_id")
) WITH (
OIDS=FALSE
);

CREATE TABLE "agent_loadmore_methods" (
  "id" serial NOT NULL,
  "agent_id" bigint NOT NULL,
  "method" varchar(50) NOT NULL,
  "value" varchar(100),
  "xpath" varchar(100),
  CONSTRAINT agent_loadmore_methods_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);


ALTER TABLE "products_agents" ADD CONSTRAINT "products_agents_fk0" FOREIGN KEY ("product_id") REFERENCES "products"("id");
ALTER TABLE "products_agents" ADD CONSTRAINT "products_agents_fk1" FOREIGN KEY ("agent_id") REFERENCES "agents"("id");



ALTER TABLE "specific_details" ADD CONSTRAINT "specific_details_fk0" FOREIGN KEY ("specific_id") REFERENCES "product_specifics"("id");


ALTER TABLE "require_terms" ADD CONSTRAINT "require_terms_fk0" FOREIGN KEY ("require_id") REFERENCES "crawling_requires"("id");

ALTER TABLE "require_formats" ADD CONSTRAINT "require_formats_fk0" FOREIGN KEY ("require_id") REFERENCES "crawling_requires"("id");

ALTER TABLE "agent_rules" ADD CONSTRAINT "agent_rules_fk0" FOREIGN KEY ("agent_id") REFERENCES "agents"("id");
ALTER TABLE "agent_rules" ADD CONSTRAINT "agent_rules_fk1" FOREIGN KEY ("require_id") REFERENCES "crawling_requires"("id");

ALTER TABLE "input_styles" ADD CONSTRAINT "input_styles_fk0" FOREIGN KEY ("attribute_id") REFERENCES "attributes"("id");
ALTER TABLE "input_styles" ADD CONSTRAINT "input_styles_fk1" FOREIGN KEY ("value_id") REFERENCES "values"("id");

ALTER TABLE "agent_loadmore_methods" ADD CONSTRAINT "agent_loadmore_methods_pk0" FOREIGN KEY ("agent_id") REFERENCES "agents"("id");

-- crawling_required

Insert into crawling_requires values(default, 'PRI', 'Price');
Insert into crawling_requires values(default, 'NAM', 'Name');

-- require_terms

Insert into require_terms values(default, 1, 'đ');
Insert into require_terms values(default, 1, 'vnđ');
Insert into require_terms values(default, 1, 'vnd');
Insert into require_terms values(default, 1, '₫');
Insert into require_terms values(default, 2, 'Tên mặt hàng :');
Insert into require_terms values(default, 2, 'Tên sản phẩm :');
Insert into require_terms values(default, 2, 'query-input');

-- term-format
Insert into require_formats values(default, 1, '(\d+)(\s*${term})');
Insert into require_formats values(default, 1, '(((\d+)(\.|,))+\d+)(\s*${term})');
Insert into require_formats values(default, 2, '(${term} ).*');

-- product_specifics
Insert into product_specifics values(default, 'COL', 'Color');
Insert into product_specifics values(default, 'MEM', 'Memory');

-- specific_details
Insert into specific_details values(default, 1, 'blue');
Insert into specific_details values(default, 1, 'yellow');
Insert into specific_details values(default, 1, 'gold');
Insert into specific_details values(default, 1, 'silver');
Insert into specific_details values(default, 1, 'black');
Insert into specific_details values(default, 1, 'green');
Insert into specific_details values(default, 1, 'white');
Insert into specific_details values(default, 1, 'orange');
Insert into specific_details values(default, 2, '8GB');
Insert into specific_details values(default, 2, '16GB');
Insert into specific_details values(default, 2, '32GB');
Insert into specific_details values(default, 2, '64GB');
Insert into specific_details values(default, 2, '128GB');
Insert into specific_details values(default, 2, '256GB');

-- agents
Insert into agents values(default, 'tgdd', 'www.thegioididong.com', 'https://www.thegioididong.com/tim-kiem?key=bla');
Insert into agents values(default, 'vienthonga', 'www.vienthonga.vn', 'https://vienthonga.vn/tim-kiem?q=bla');
Insert into agents values(default, 'fptshop', 'www.fptshop.com.vn', 'https://fptshop.com.vn/tim-kiem/bla');

-- attributes
Insert into "attributes" values(default, 'id');
Insert into "attributes" values(default, 'class');

-- values
Insert into "values" values(default, 'search-keyword');
Insert into "values" values(default, 'searchvta');
Insert into "values" values(default, 'fs-stxt');

-- input_styles
Insert into input_styles values(1, 1);
Insert into input_styles values(1, 2);
Insert into input_styles values(1, 3);
Insert into input_styles values(2, 1);
Insert into input_styles values(2, 2);
Insert into input_styles values(2, 3);

-- placeholders
Insert into placeholders values(default, 'tìm');

-- ignored_words
Insert into ignored_words values(default, 'cũ');
Insert into ignored_words values(default, 'mới');
Insert into ignored_words values(default, '100%');
Insert into ignored_words values(default, 'chính hãng');
Insert into ignored_words values(default, 'Hàn Quốc');
Insert into ignored_words values(default, 'product');

-- agent_rules
Insert into agent_rules values(1, 1, '(((\d+)(\.|,))+\d+)(\s*₫)', 1);
Insert into agent_rules values(1, 2, '{query-only}', 0);
Insert into agent_rules values(2, 1, '(((\d+)(\.|,))+\d+)(\s*đ)', 1);
Insert into agent_rules values(2, 2, '{query-only}', 0);
Insert into agent_rules values(3, 1, '(((\d+)(\.|,))+\d+)(\s*₫)', 1);
Insert into agent_rules values(3, 2, '{query-only}', 0);

-- agent_loadmore_methods
Insert into agent_loadmore_methods values(default, 1, 'ajax', 'ShowMoreProductResult()', '//a[@href="javascript:ShowMoreProductResult();"]');
Insert into agent_loadmore_methods values(default, 2, 'ajax', null, '//div[@id="SearchproductPager"]/button[@title="Xem thêm"]');
Insert into agent_loadmore_methods values(default, 3, 'ajax', null, '//a[@data-type="san-pham"]');

-- removed_tags
Insert into removed_tags values(default, 'del');

-- ignored_tags
Insert into ignored_tags values(default, 'br');

-- format_tags
Insert into format_tags values(default, 'acronym');
Insert into format_tags values(default, 'abbr');
Insert into format_tags values(default, 'b');
Insert into format_tags values(default, 'bdi');
Insert into format_tags values(default, 'bdo');
Insert into format_tags values(default, 'big');
Insert into format_tags values(default, 'blockquote');
Insert into format_tags values(default, 'center');
Insert into format_tags values(default, 'cite');
Insert into format_tags values(default, 'code');
Insert into format_tags values(default, 'del');
Insert into format_tags values(default, 'dfn');
Insert into format_tags values(default, 'em');
Insert into format_tags values(default, 'font');
Insert into format_tags values(default, 'ins');
Insert into format_tags values(default, 'kbd');
Insert into format_tags values(default, 'mark');
Insert into format_tags values(default, 'q');
Insert into format_tags values(default, 'rp');
Insert into format_tags values(default, 'rt');
Insert into format_tags values(default, 'ruby');
Insert into format_tags values(default, 's');
Insert into format_tags values(default, 'samp');
Insert into format_tags values(default, 'small');
Insert into format_tags values(default, 'strike');
Insert into format_tags values(default, 'sub');
Insert into format_tags values(default, 'time');
Insert into format_tags values(default, 'tt');

-- products

Insert into products values(default, 'iPhone 5s 16GB', null, null, null, null);
Insert into products values(default, 'iPhone 6 32GB (2017)', null, null, null, null);
Insert into products values(default, 'iPhone 6s 64GB', null, null, null, null);
Insert into products values(default, 'iPhone 7 128GB PRODUCT RED', null, null, null, null);
Insert into products values(default, 'iPhone 7 Plus 128GB PRODUCT RED', null, null, null, null);

--DROP SCHEMA public CASCADE;
--CREATE SCHEMA public;

select * from products where lower("name") like lower('%iphone%');

INSERT INTO products_agents VALUES (1, 3, 1, 13000000, 'https://www.thegioididong.com/dtdd/iphone-6s-plus-32gb');
INSERT INTO products_agents VALUES (2, 3, 2, 12900000, 'https://vienthonga.vn/iphone-6s-plus-32gb-gold.html');
INSERT INTO products_agents VALUES (3, 3, 3, 12300000, 'https://www.didongviet.vn/iphone-6s-plus-16gb-quoc-te-chua-active-troi-bao-hanh.html');
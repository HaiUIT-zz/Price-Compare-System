CREATE TABLE "agents" (
  "id" serial NOT NULL,
  "code" varchar(50) NOT NULL,
  "name" varchar(50) NOT NULL,
  "search_url" varchar(200) NOT NULL,
  CONSTRAINT agents_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "products" (
  "id" serial NOT NULL,
  "name" varchar(100) NOT NULL,
  "image" varchar(200) NOT NULL,
  "visit_count" integer NOT NULL,
  "rating" numeric(4,2) NOT NULL,
  CONSTRAINT products_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "products_agents" (
  "product_id" bigint NOT NULL,
  "argent_id" bigint NOT NULL,
  "price" DECIMAL NOT NULL,
  "url" varchar NOT NULL,
  CONSTRAINT products_agents_pk PRIMARY KEY ("product_id","argent_id")
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
  "specific_id" bigint NOT NULL,
  "possible_text" varchar(50) NOT NULL,
  CONSTRAINT specific_details_pk PRIMARY KEY ("specific_id","possible_text")
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



CREATE TABLE "argent_rules" (
  "id" serial NOT NULL,
  "argent_id" bigint NOT NULL,
  "require_id" bigint NOT NULL,
  "format" varchar NOT NULL,
  "rule_index" integer NOT NULL,
  CONSTRAINT argent_rules_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);


ALTER TABLE "products_agents" ADD CONSTRAINT "products_agents_fk0" FOREIGN KEY ("product_id") REFERENCES "products"("id");
ALTER TABLE "products_agents" ADD CONSTRAINT "products_agents_fk1" FOREIGN KEY ("argent_id") REFERENCES "agents"("id");



ALTER TABLE "specific_details" ADD CONSTRAINT "specific_details_fk0" FOREIGN KEY ("specific_id") REFERENCES "product_specifics"("id");


ALTER TABLE "require_terms" ADD CONSTRAINT "require_terms_fk0" FOREIGN KEY ("require_id") REFERENCES "crawling_requires"("id");

ALTER TABLE "require_formats" ADD CONSTRAINT "require_formats_fk0" FOREIGN KEY ("require_id") REFERENCES "crawling_requires"("id");

ALTER TABLE "argent_rules" ADD CONSTRAINT "argent_rules_fk0" FOREIGN KEY ("argent_id") REFERENCES "agents"("id");
ALTER TABLE "argent_rules" ADD CONSTRAINT "argent_rules_fk1" FOREIGN KEY ("require_id") REFERENCES "crawling_requires"("id");

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
Insert into specific_details values(1, 'blue');
Insert into specific_details values(1, 'yellow');
Insert into specific_details values(1, 'gold');
Insert into specific_details values(1, 'silver');
Insert into specific_details values(1, 'black');
Insert into specific_details values(1, 'green');
Insert into specific_details values(1, 'white');
Insert into specific_details values(1, 'orange');
Insert into specific_details values(2, '8GB');
Insert into specific_details values(2, '16GB');
Insert into specific_details values(2, '32GB');
Insert into specific_details values(2, '64GB');
Insert into specific_details values(2, '128GB');

-- agents
Insert into agents values(default, 'tgdd', 'thegioididong.com', 'https://www.thegioididong.com/tim-kiem?key=${query}');
Insert into agents values(default, 'vienthonga', 'vienthonga.vn', 'https://vienthonga.vn/tim-kiem?q=${query}');



--DROP SCHEMA public CASCADE;
--CREATE SCHEMA public;


INSERT INTO work_define(define_id,work_name,type,enabled,version,process) VALUES
    ("func.corp.create","创建从业机构","data",true,0,false),
    ("func.corp.update","修改从业机构信息","data",true,0,false),
    ("func.corp.record.update","修改从业机构备案信息","data",true,0,false),
    ("func.corp.record.delete","删除从业机构备案","action",true,0,false),
    ("func.corp.record.disable","禁用从业机构备案","action",true,0,false),
    ("func.corp.record.enable","启用从业机构备案","action",true,0,false),
    ("func.corp.record.create","从业机构备案","data",true,0,false),
    ("func.corp.delete","删除从业机构","action",true,0,false),

    ("func.building.project.create","创建工程项目","data",true,0,false),
    ("func.building.project.update","修改工程项目信息","data",true,0,false),

    ("func.building.project.delete","删除工程项目","action",true,0,false),
    ("func.building.project.developer","更改开发单位","business",true,0,false),


    ("func.building.build.move","楼幢迁移","compose",true,0,false),
    ("func.building.build.create","创建楼幢","data",true,0,false),
    ("func.building.build.update","修改楼幢信息","data",true,0,false),
    ("func.building.build.delete","删除楼幢","action",true,0,false),

    ("func.building.build.house.update","楼幢分层分户修改","data",true,0,false),

    ("func.building.house.delete","删除房屋","action",true,0,false),
    ("func.building.house.update","修改房屋信息","data",true,0,false),

    ("func.building.register.remove","移除房屋初始登记备案","action",true,0,false),
    ("func.building.house.register","房屋初始登记备案","data",true,0,false),

    ("process_project_license","预销售许可证业务","business",true,0,true),

    ("func.limit.create","建立预警","action",true,0,false),
    ("func.limit.cancel","预警撤消","action",true,0,false),
    ("func.limit.mortgage.create","抵押预警","business",true,0,false),
    ("func.limit.mortgage.cancel","抵押预警撤消","business",true,0,false),
    ("func.limit.seizure.create","查封预警","business",true,0,false),
    ("func.limit.seizure.cancel","查封预警撤消","business",true,0,false),

    ("process_sale_contract_new","商品房合同备案","business",true,0,false),
    ("process_sale_contract_new_cancel","商品房合同备案撤消","business",true,0,false),
    ("process_sale_contract_old","存量房合同备案","business",true,0,false),
    ("process_sale_contract_old_cancel","存量房合同备案撤消","business",true,0,false);

INSERT INTO attachment_define(id,define_id,name,must,version,description) VALUES
    (1,"process_project_license","营业执照",true,0,"营业执照说明"),
    (2,"process_project_license","资质证明",true,0,"资质证明说明")
    ;
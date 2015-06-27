#coding:utf-8

require "json"
require "levenshtein"
require 'unicode'


phone_regexp = ["телефон", "теле", "phone", "факс", "fax" ]

if __FILE__ == $0
	ARGV.each do |file|
		geo = {}
		data = nil
		begin
			data = JSON.load(File.open(file))
		rescue
			next
		end
		next if data == nil
		next if data.size == 0
		phones = []
		data.each do |d|
			local_phone = []
			d.each do |key,val|
				if val != nil
					phone_idx = phone_regexp.map{|x| Unicode::downcase(key).index(x)}.uniq
					if phone_idx.size > 1 || phone_idx[0] != nil 
						if (val.match(/^[0-9\-\+\s\(\)]+$/))
							local_phone.push(val)
						end
					end
				end
			end
			phones.push({"phone" => local_phone})
		end
		fname = File.basename(file,".*")
		File.write(fname + ".phone.json", phones.to_json)
	end
end
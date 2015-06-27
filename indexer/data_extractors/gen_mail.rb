#coding:utf-8

require "json"
require "levenshtein"


mail_regexp = /([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4})/

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
		mails = []
		data.each do |d|
			local_mail = []
			d.each do |key,val|
				if val != nil
					mail = val.match(mail_regexp)
					if mail != nil 
						local_mail.push(mail[1])
					end
				end
			end
			mails.push({"e-mail" => local_mail})
		end
		fname = File.basename(file,".*")
		File.write(fname + ".e-mail.json", mails.to_json)
	end
end